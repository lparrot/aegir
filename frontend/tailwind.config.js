const merge = require("lodash/merge");
const defaultTheme = require("./tailwind/themes/default");

/** @type {import("tailwindcss").Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx,scss}",
  ],

  theme: merge({}, defaultTheme, {
    extend: {
      transitionProperty: {
        "height": "height",
      },
    },
  }),

  plugins: [
    require("@tailwindcss/forms"),
    require("tailwindcss-font-inter"),
    require("tailwind-scrollbar"),
  ],
};
