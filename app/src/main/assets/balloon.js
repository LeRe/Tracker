
            var placemark$BALLOON_INDEX$ = new ymaps.Placemark(
                [$BALLOON_LATITUDE$, $BALLOON_LONGITUDE$],
            {
                balloonContent: '$BALLOON_CONTENT$'
            },
            {
                iconColor: '$ICON_COLOR$'
            });
            myMap.geoObjects.add(placemark$BALLOON_INDEX$);
            placemark$BALLOON_INDEX$.balloon.close();
