importScripts('geral.js');
importScripts('vsop87.min.js');
importScripts('astro.js');

self._object = {};

self._object.acc = 0;
self._object.calculando = false;
self._object.interval = null;

self.addEventListener('message', function (e) {
    var data = e.data;

    function cancelarCalculo() {
        if (self._object.interval !== null && self._object.calculando) {
            self._object.calculando = false;
            self.clearInterval(self._object.interval);
            self._object.interval = null;
        }
    }

    function getAcc() {
        self.postMessage({cmd: 'getAcc', acc: self._object.acc, calculando: self._object.calculando});
    }

    function eclipseSolar(julian) {
        getSolLuaAnguloDiferenca(julian, function (julianIt) {
            var solarEclipse = ON_DAED.ASTRO.getSolarEclipse(julianIt);
            return !solarEclipse.noEclipse;
        });
    }

    function eclipseLunar(julian) {
        getSolLuaAnguloDiferenca(julian, function (julianIt) {
            var lunarEclipse = ON_DAED.ASTRO.getLunarEclipse(julianIt);
            return !lunarEclipse.noEclipse;
        });
    }

    function getSolLuaAnguloDiferenca(julian, fnInterval, extMinuteInterval) {
        if (!self._object.calculando) {
            self._object.calculando = true;

            var minInterval = extMinuteInterval || (60 * 24 * 28);

            var stepSearch = 0.000694 * minInterval; // 1 min * x min

            var start = julian;
            self._object.acc = start;

            var found = false;

            self._object.interval = self.setInterval(function () {
                if (self._object.calculando) {
                    
                    found = fnInterval(self._object.acc);

                    if (found) {
                        self.clearInterval(self._object.interval);
                        self._object.calculando = false;
                        self._object.interval = null;
                    } else {
                        self._object.acc += stepSearch;
                    }

                }
            }, 100);
        }
    }

    function observacaoEquatorial(date) {
        var position = ON_DAED.ASTRO.getSolarSystemEquatorialCoordinates(date);
        position.cmd = 'observacaoEquatorial';
        position.julian = date;

        self.postMessage(position);
    }

    function transitoSolar(julian, localLongitude, localLatitude) {
        var transit = ON_DAED["ASTRO"].getTransit(-localLongitude, localLatitude, julian, ON_DAED.ASTRO.SolarSystemBody.SUN);
        transit.cmd = 'transitoSolar';
        self.postMessage(transit);
    }

    function fasesDaLua(julian) {
        var nova = ON_DAED.ASTRO.getNextMoonPhaseFromJulian(julian, ON_DAED.ASTRO.MoonPhases.NEW);
        var crescenteVar = ON_DAED.ASTRO.MoonPhases.FIRST_QUARTER;
        var cheiaVar = ON_DAED.ASTRO.MoonPhases.FULL;
        var minguanteVar = ON_DAED.ASTRO.MoonPhases.LAST_QUARTER;

        var julianInt = parseInt(julian);
        var julianOriginal = julian;


        while (nova < julianOriginal) {
            julian = parseFloat(julian) + 30;
            nova = ON_DAED.ASTRO.getNextMoonPhaseFromJulian(julian, ON_DAED.ASTRO.MoonPhases.NEW);
        }

        var diff = parseInt(Math.abs(parseInt(nova) - julianInt) / 7);

        if (diff > 0) {
            minguanteVar = -(1 - ON_DAED.ASTRO.MoonPhases.LAST_QUARTER);
            diff--;
        }

        if (diff > 0) {
            cheiaVar = -(1 - ON_DAED.ASTRO.MoonPhases.FULL);
            diff--;
        }

        if (diff > 0) {
            crescenteVar = -(1 - ON_DAED.ASTRO.MoonPhases.FIRST_QUARTER);
        }

        var fases = {
            nova: nova,
            crescente: ON_DAED.ASTRO.getNextMoonPhaseFromJulian(julian, crescenteVar),
            cheia: ON_DAED.ASTRO.getNextMoonPhaseFromJulian(julian, cheiaVar),
            minguante: ON_DAED.ASTRO.getNextMoonPhaseFromJulian(julian, minguanteVar)
        };

        fases.cmd = 'fasesDaLua';

        self.postMessage(fases);
    }

    function faseAtualDaLua(julian) {
        var faseAtualLua = {};

        var atual = ON_DAED.ASTRO.getIlluminatedFractionOfMoonDiskFromJulian(julian);

        faseAtualLua.fase = parseInt(atual * 100) + '% do disco lunar iluminado';
        faseAtualLua.cmd = 'faseAtualLua';

        self.postMessage(faseAtualLua);
    }

    switch (data.cmd) {
        case 'observacaoEquatorial':
            observacaoEquatorial(data.data);
            break;
        case 'transitoSolar':
            transitoSolar(data.data, data.longitude, data.latitude);
            break;
        case 'fasesDaLua':
            fasesDaLua(data.data);
            break;
        case 'faseAtualLua':
            faseAtualDaLua(data.data);
            break;
        case 'getAcc':
            getAcc();
            break;
        case 'eclipseSolar':
            eclipseSolar(data.data);
            break;
        case 'eclipseLunar':
            eclipseLunar(data.data);
            break;
        case 'cancelarCalculo':
            cancelarCalculo();
            break;
    }

});