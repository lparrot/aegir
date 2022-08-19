const colors = require("tailwindcss/colors");

/** @type {import("tailwindcss").Config} */
module.exports = {
  content: [
    "./projects/app/src/**/*.{html,ts}",
    "./projects/ui/src/**/*.{html,ts}",
  ],

  theme: {
    extend: {
      colors: {
        primary: colors.blue["500"],
      },
    },
  },

  plugins: [],
};
