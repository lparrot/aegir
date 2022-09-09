export function checkStringNotEmpty(value: string) {
  return value != null && value.trim() !== "";
}

export const getInitials = (text: string) => {
  if (text == null) {
    return "";
  }

  const names = text.split(" ");
  let initials = names[0].substring(0, 1).toUpperCase();

  if (names.length > 1) {
    initials += names[names.length - 1].substring(0, 1).toUpperCase();
  }
  return initials;
};
