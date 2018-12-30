"use strict";

var humView = $('#hum');
var tempView = $('#temp');
var timeView = $('#time');
var chartView = $('#chart');


function getLastMeasurement() {
    return $.ajax({
        url: '/measurement/last',
        dataType: 'JSON',
        success: function (data) {
            return data;
        },
        error: function () {
            alert("Connection error !!!");
        }
    });
}

function createChart() {
    var ctx = document.getElementById('chart');
    return new Dygraph(ctx, [
        [new Date(1474200000000), 20, 49],
        [new Date(1474200010000), 22, 50],
        [new Date(1474200020000), 20, 48],
        [new Date(1474200030000), 22, 48],
        [new Date(1474200040000), 22, 50],
        [new Date(1474200050000), 21, 50],
        [new Date(1474200060000), 21, 49],
        [new Date(1474200070000), 21, 49],
        [new Date(1474200080000), 22, 50],
        [new Date(1474200090000), 22, 50]]
            , {
                drawPoints: true,
                labels: ['t', 'H', 'T'],
                title: "LAST 10",
                titleHeight: 80,
                ylabel: 'Humidity [%]',
                y2label: 'Temperature [&deg;C]',
                xlabel: 'Time',
                xLabelHeight: 22,
                yLabelWidth: 22,
                gridLineColor: '#FFFFFF',
                axes: {
                    x: {
                        axisLabelFontSize: 16,
                        axisLineColor: '#FFFFFF',
                        axisLabelColor: '#FFFFFF',
                    },
                    y: {
                        axisLabelFontSize: 16,
                        axisLabelColor: '#006ED0',
                        fillGraph: true,
                        axisLineColor: '#FFFFFF',
                        axisLabelWidth: 80
                    },
                    y2: {
                        axisLabelFontSize: 16,
                        axisLabelColor: '#dc3545',
                        fillGraph: true,
                        gridLinePattern: [2, 2],
                        axisLineColor: '#FFFFFF',
                        axisLabelWidth: 80
                    }
                },
                series: {
                    'H': {
                        axis: 'y1'
                    },
                    'T': {
                        axis: 'y2'
                    }
                },
                colors: [
                    "rgb(0, 110, 208)",
                    "rgb(220, 53, 69)"]
            });
}

function getAllMeasurements() {
    return $.ajax({
        url: '/measurement',
        dataType: 'JSON',
        success: function (data) {
            return data;
        },
        error: function () {
            alert("Connection error !!!");
        }
    });
}

function renderMeasurementChartView(data, chart) {
    var measurements = [];
    chartView.fadeTo(500, 0.1);
    $.each(data, function (i, d) {
        measurements.push([new Date(d['date']), d['humidityPercentage'], d['temperatureInCelsius']]);
    });

    chart.updateOptions({'file': measurements});
    chartView.fadeTo(500, 1);
}

function renderLastMeasurementView(data) {
    humView.fadeTo(500, 0.1);
    tempView.fadeTo(500, 0.1);
    timeView.fadeTo(500, 0.1);

    humView.text(data['humidityPercentage']);
    tempView.text(data['temperatureInCelsius']);
    var date = new Date(data['date']);
    timeView.text(date.toLocaleTimeString());

    transform_number($('#hum'), 300, 'flex');
    transform_number($('#temp'), 300, 'flex');

    humView.fadeTo(500, 1);
    tempView.fadeTo(500, 1);
    timeView.fadeTo(500, 1);
}

function controlLastMeasurement() {
    getLastMeasurement().then(function (data) {
        renderLastMeasurementView(data);
    });
}

function controlAllMeasurements(chart) {
    getAllMeasurements().then(function (data) {
        renderMeasurementChartView(data, chart);
    });
}

$(function () {
    var chart = createChart();
    controlLastMeasurement();
    controlAllMeasurements(chart);
    setInterval(function () {
        controlLastMeasurement();
        controlAllMeasurements(chart);
    }, 5000);
});
