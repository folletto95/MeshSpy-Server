#!/bin/sh
set -e
# Download Leaflet distribution and extract required assets
VERSION="1.9.4"
BASE_URL="https://unpkg.com/leaflet@${VERSION}/dist"
TARGET_DIR="$(dirname "$0")/../admin/src/main/resources/static/leaflet"
mkdir -p "$TARGET_DIR"

for FILE in leaflet.js leaflet.css; do
  if [ ! -f "$TARGET_DIR/$FILE" ]; then
    echo "Downloading $FILE"
    curl -L "$BASE_URL/$FILE" -o "$TARGET_DIR/$FILE"
  fi
done

IMAGES_DIR="$TARGET_DIR/images"
mkdir -p "$IMAGES_DIR"
for IMG in marker-icon.png marker-icon-2x.png marker-shadow.png layers.png layers-2x.png; do
  if [ ! -f "$IMAGES_DIR/$IMG" ]; then
    echo "Downloading $IMG"
    curl -L "$BASE_URL/images/$IMG" -o "$IMAGES_DIR/$IMG"
  fi
done

if [ ! -f "$TARGET_DIR/blank.png" ]; then
  echo "Creating blank.png"
  printf '\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x01\x00\x00\x00\x01\x00\x08\x06\x00\x00\x00\x1f\xf3\xffa\x00\x00\x00\x0bIDATx\x9ccddbf\xa0\x040Q\xa4\x91\x81\x81\x81\x00\x00\x01\x0e\x00\x01f<\xde\xa6\x00\x00\x00\x00IEND\xaeB`\x82' > "$TARGET_DIR/blank.png"
fi

echo "Leaflet assets downloaded to $TARGET_DIR"
