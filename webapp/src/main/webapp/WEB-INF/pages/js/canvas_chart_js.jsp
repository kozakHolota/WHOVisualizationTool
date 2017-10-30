<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

var CanvasChart = function () {
    var margin = { top: 40, left: 75, right: 0, bottom: 75 };
    var chartHeight, chartWidth, yMax, xMax, data;
    var maxYValue = 0;
    var ratio = 0;
    var renderType = { lines: 'lines', points: 'points' };

    var render = function(years, main_chart_points, second_chart_points) {
        data = {            title: stat_title,
                            xLabel: year,
                            yLabel: stat_losung,
                            labelFont: '19pt Arial',
                            dataPointFont: '10pt Arial',
                            renderTypes: [CanvasChart.renderType.lines, CanvasChart.renderType.points],
                            dataPoints: []
                           };

        $.each(years, function(index, element) {
        var portion = {x: element.toString(), y: [main_chart_points[index], second_chart_points[index]]};
        data.dataPoints.push(portion);
        });

        getMaxDataYValue();
        chartHeight = canvas.getAttribute('height');
        chartWidth = canvas.getAttribute('width');
        xMax = chartWidth - (margin.left + margin.right);
        yMax = chartHeight - (margin.top + margin.bottom);
        ratio = yMax / maxYValue;
        ctx.clearRect(0, 0, canvas.width, canvas.height)
        ctx.restore();
        ctx.save();
        renderChart();
    };

    var renderChart = function () {
        renderBackground();
        renderText();
        renderLinesAndLabels();

        //render data based upon type of renderType(s) that client supplies
        if (data.renderTypes == undefined || data.renderTypes == null) data.renderTypes = [renderType.lines];
        for (var i = 0; i < data.renderTypes.length; i++) {
            renderData(data.renderTypes[i]);
        }
    };

    var getMaxDataYValue = function () {
        $.each(data.dataPoints, function(index, element) {
            if (element.y[0] > maxYValue) { maxYValue = element.y[0]; }
            else if (element.y[1] > maxYValue) { maxYValue = element.y[1]; }
        });
    };

    var renderBackground = function() {
        var lingrad = ctx.createLinearGradient(margin.left, margin.top, xMax - margin.right, yMax);
        lingrad.addColorStop(0.0, '#D4D4D4');
        lingrad.addColorStop(0.2, '#fff');
        lingrad.addColorStop(0.8, '#fff');
        lingrad.addColorStop(1, '#D4D4D4');
        ctx.fillStyle = lingrad;
        ctx.fillRect(margin.left, margin.top, xMax - margin.left, yMax - margin.top);
        ctx.fillStyle = 'black';
    };

    var renderText = function() {
        var labelFont = (data.labelFont != null) ? data.labelFont : '20pt Arial';
        ctx.font = labelFont;
        ctx.textAlign = "center";

        //Title
        var txtSize = ctx.measureText(data.title);
        ctx.fillText(data.title, (chartWidth / 2), (margin.top / 2));

        //X-axis text
        txtSize = ctx.measureText(data.xLabel);
        ctx.fillText(data.xLabel, margin.left + (xMax / 2) - (txtSize.width / 2), yMax + (margin.bottom / 1.2));

        //Y-axis text
        ctx.save();
        ctx.rotate(-Math.PI / 2);
        ctx.font = labelFont;
        ctx.fillText(data.yLabel, (yMax / 2) * -1, margin.left / 4);
        ctx.restore();
    };

    var renderLinesAndLabels = function () {
        //Vertical guide lines
        var yInc = yMax / data.dataPoints.length;
        var yPos = 0;
        var yLabelInc = (maxYValue * ratio) / data.dataPoints.length;
        var xInc = getXInc();
        var xPos = margin.left;
        $.each(data.dataPoints, function(i, element) {
            yPos += (i == 0) ? margin.top : yInc;
            //Draw horizontal lines
            drawLine(margin.left, yPos, xMax, yPos, '#E8E8E8');

            //y axis labels
            ctx.font = (data.dataPointFont != null) ? data.dataPointFont : '10pt Calibri';
            var txt = Math.round(maxYValue - ((i == 0) ? 0 : yPos / ratio));
            var txtSize = ctx.measureText(txt);
            ctx.fillText(txt, margin.left - ((txtSize.width >= 14) ? txtSize.width : 10) - 7, yPos + 4);

            //x axis labels
            txt = data.dataPoints[i].x;
            txtSize = ctx.measureText(txt);
            ctx.fillText(txt, xPos, yMax + (margin.bottom / 3));
            xPos += xInc;
        });

        //Vertical line
        drawLine(margin.left, margin.top, margin.left, yMax, 'black');

        //Horizontal Line
        drawLine(margin.left, yMax, xMax, yMax, 'black');
    };

    var renderData = function(type) {
        var xInc = getXInc();
        var prevX = 0,
            prevY1 = 0,
            prevY2 = 0;

        $.each(data.dataPoints, function(i, element) {
            var pt = element;
            var ptY1 = (maxYValue - pt.y[0]) * ratio;
            if (ptY1 < margin.top) ptY1 = margin.top;
            var ptY2 = (maxYValue - pt.y[1]) * ratio;
            if (ptY1 < margin.top) ptY1 = margin.top;
            var ptX = (i * xInc) + margin.left;

            if (i > 0 && type == renderType.lines) {
                //Draw connecting lines
                drawLine(ptX, ptY1, prevX, prevY1, 'red', 2);
                drawLine(ptX, ptY2, prevX, prevY2, 'green', 2);
            }

            if (type == renderType.points) {
                var radgrad = ctx.createRadialGradient(ptX, ptY1, 8, ptX - 5, ptY1 - 5, 0);
                radgrad.addColorStop(0, 'Green');
                radgrad.addColorStop(0.9, 'White');
                ctx.beginPath();
                ctx.fillStyle = radgrad;
                //Render circle
                ctx.arc(ptX, ptY1, 4, 0, 2 * Math.PI, false)
                ctx.fill();
                ctx.strokeStyle = '#000';
                ctx.stroke();
                ctx.closePath();

                radgrad = ctx.createRadialGradient(ptX, ptY2, 8, ptX - 5, ptY2 - 5, 0);
                radgrad.addColorStop(0, 'Red');
                radgrad.addColorStop(0.9, 'White');
                ctx.beginPath();
                ctx.fillStyle = radgrad;
                //Render circle
                ctx.arc(ptX, ptY2, 4, 0, 2 * Math.PI, false)
                ctx.fill();
                ctx.lineWidth = 1;
                ctx.strokeStyle = '#000';
                ctx.stroke();
                ctx.closePath();
            }

            prevX = ptX;
            prevY1 = ptY1;
            prevY2 = ptY2;
        });
    };

    var getXInc = function() {
        return Math.round(xMax / data.dataPoints.length) - 1;
    };

    var drawLine = function(startX, startY, endX, endY, strokeStyle, lineWidth) {
        if (strokeStyle != null) ctx.strokeStyle = strokeStyle;
        if (lineWidth != null) ctx.lineWidth = lineWidth;
        ctx.beginPath();
        ctx.moveTo(startX, startY);
        ctx.lineTo(endX, endY);
        ctx.stroke();
        ctx.closePath();
    };

    return {
        renderType: renderType,
        render: render
    };
} ();