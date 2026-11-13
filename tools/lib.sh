# shellcheck shell=bash
skipped() { echo "[check] SKIPPED $1: $2"; }

require() {
  command -v "$1" >/dev/null 2>&1 || {
    printf "'%s' is required for this check but is not on PATH -- install it: %s\n" "$1" "$2" >&2
    exit 1
  }
}
