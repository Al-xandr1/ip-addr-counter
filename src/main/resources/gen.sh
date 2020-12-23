#!/usr/bin/env bash

#set -x
file="${6:-ips.txt}"
rm "$file"
for ((i=1; i<="${1:-1}"; i++ )); do
  for ((j=1; j<="${2:-1}"; j++ )); do
    for ((k=1; k<="${3:-255}"; k++ )); do
      for ((l=1; l<="${4:-255}"; l++)); do
        for ((r=1; r<="${5:-1}"; r++)); do
            echo "$i.$j.$k.$l"
        done
      done
    done
  done
done>>"$file"

