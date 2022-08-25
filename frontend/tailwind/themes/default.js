const colors = require("tailwindcss/colors");

module.exports = {
  extend: {
    colors: {
      "primary": colorsWithDefault(colors.gray),
      "secondary": colorsWithDefault(colors.indigo),
      "success": colorsWithDefault(colors.green),
      "info": colorsWithDefault(colors.blue),
      "warn": colorsWithDefault(colors.orange),
      "danger": colorsWithDefault(colors.red),
    },
  },
};

function colorsWithDefault(color) {
  return { ...color, DEFAULT: color["500"] };
}
