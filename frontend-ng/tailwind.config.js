const colors = require("tailwindcss/colors");
const defaultTheme = require("tailwindcss/defaultTheme");

/** @type {import("tailwindcss").Config} */
module.exports = {
  content: [
    "./projects/app/src/**/*.{html,ts,scss}",
    "./projects/ui/src/**/*.{html,ts,scss}",
  ],

  theme: {
    extend: {
      colors: {
        primary: colors.gray["800"],
      },
    },
  },

  plugins: [
    require("tailwind-scrollbar"),
    require("tailwindcss-font-inter"),
  ],
};
