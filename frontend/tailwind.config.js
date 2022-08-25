const theme = require("./tailwind/themes/default");

/** @type {import("tailwindcss").Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx,scss}",
  ],

  theme,

  plugins: [
    require("@tailwindcss/forms"),
    require("tailwindcss-font-inter"),
    require("tailwind-scrollbar"),
  ],
};
