dataset2D = [
  [5, 20],
  [480, 90],
  [250, 50],
  [100, 33],
  [330, 95],
  [410, 12],
  [475, 44],
  [25, 67],
  [85, 21],
  [220, 88],
  [600, 150],
];

const svgWidth = 500;
const svgHeight = 300;
const svg = d3
  .select("body")
  .append("svg")
  .attr("width", svgWidth)
  .attr("height", svgHeight);

const padding = 20;
const xScale = d3
  .scaleLinear()
  .domain([0, d3.max(dataset2D, (d) => d[0])])
  .range([padding, svgWidth - padding * 3])
  .nice();

const yScale = d3
  .scaleLinear()
  .domain([0, d3.max(dataset2D, (d) => d[1])])
  .range([svgHeight - padding, padding])
  .nice();

const rScale = d3
  .scaleSqrt()
  .domain([0, d3.max(dataset2D, (d) => d[1])])
  .range([2, 5]);

svg
  .selectAll("circle")
  .data(dataset2D)
  .enter()
  .append("circle")
  .attr("cx", (d) => xScale(d[0]))
  .attr("cy", (d) => yScale(d[1]))
  .attr("r", (d) => rScale(d[1]))
  .attr("fill", "red");

svg
  .selectAll("text")
  .data(dataset2D)
  .enter()
  .append("text")
  .text((d) => `(${d[0]},${d[1]})`)
  .attr("x", (d) => xScale(d[0]) + 5)
  .attr("y", (d) => yScale(d[1]) + 5)
  .attr("font-size", "10px");

const parseTime = d3.timeParse("%m/%d/%y");
const formatTime = d3.timeFormat("%b %e");
console.log(parseTime("02/20/17"));

// Scaling timeseries data

const svg2Width = 700;
const svg2Height = 500;
const svg2 = d3
  .select("body")
  .append("svg")
  .attr("width", svg2Width)
  .attr("height", svg2Height);

const rowMapper = (d) => {
  return {
    Date: parseTime(d["Date"]),
    Amount: parseInt(d["Amount"]),
  };
};

d3.csv("data/timeseries.csv", rowMapper).then((datasetTs, error) => {
  if (error) {
    console.log("Error occurred while loading CSV", error);
  } else {
    console.log(datasetTs);
    const xScaleTs = d3
      .scaleTime()
      .domain([
        d3.min(datasetTs, (d) => d["Date"]),
        d3.max(datasetTs, (d) => d["Date"]),
      ])
      .range([padding, svg2Width - padding * 3]);

    const yScaleTs = d3
      .scaleLinear()
      .domain([0, d3.max(datasetTs, (d) => d["Amount"])])
      .range([svg2Height - padding, padding]);

    svg2
      .selectAll("circle")
      .data(datasetTs)
      .enter()
      .append("circle")
      .attr("cx", (d) => xScaleTs(d["Date"]))
      .attr("cy", (d) => yScaleTs(d["Amount"]))
      .attr("r", 5)
      .attr("fill", "blue");

    svg2
      .selectAll("text")
      .data(datasetTs)
      .enter()
      .append("text")
      .text((d) => formatTime(d["Date"]))
      .attr("x", (d) => xScaleTs(d["Date"]) - 10)
      .attr("y", (d) => yScaleTs(d["Amount"]) + 15)
      .attr("font-size", 12);
  }
});
