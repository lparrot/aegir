/** @type {import("tailwindcss").Config} */
module.exports = {
  content: [
    "./projects/app/src/**/*.{html,ts}",
    "./projects/ui/src/**/*.{html,ts}",
  ],

  theme: {
    extend: {},
  },

  plugins: [
    require("flowbite/plugin"),
  ],
};
