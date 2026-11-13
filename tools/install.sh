#!/usr/bin/env bash
set -euo pipefail

require_bun() {
  local manager required installed
  manager="$(sed -n 's/.*"packageManager"[[:space:]]*:[[:space:]]*"\\(bun@[0-9][0-9.]*\\)".*/\\1/p' package.json | head -n1)"
  required="${manager#bun@}"
  if [ -z "$required" ]; then
    echo "package.json packageManager is not a pinned bun version: '${manager}'" >&2
    exit 1
  fi
  if ! command -v bun >/dev/null 2>&1; then
    echo "bun ${required} is required but no bun is on PATH. Install it: https://bun.sh" >&2
    exit 1
  fi
  installed="$(bun --version)"
  if [ "$installed" != "$required" ]; then
    echo "bun ${required} is required (package.json packageManager), but bun ${installed} is on PATH." \
      "Run 'bun upgrade --stable' (or install bun ${required}) and retry." >&2
    exit 1
  fi
}

. "${0%/*}/uv.sh"
require_bun
uv sync --all-packages
bun install
if [ -f .pre-commit-config.yaml ]; then
  uv run --no-sync pre-commit install --install-hooks
fi
if [ -f tools/gen-types.sh ]; then
  bash tools/gen-types.sh
fi
