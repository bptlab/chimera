export function timeDiff(date) {
  var duration = new Date() - date;
  var seconds = parseInt((duration / 1000) % 60),
    minutes = parseInt((duration / (1000 * 60)) % 60),
    hours = parseInt((duration / (1000 * 60 * 60)) % 24);

  if (hours > 0) {
    return hours + "h";
  }
  if (minutes > 0) {
    return minutes + "min";
  }
  return seconds + "sec ";
}
