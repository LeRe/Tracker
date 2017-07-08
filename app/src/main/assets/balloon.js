            var placemark = new ymaps.Placemark(myMap.getCenter(), {
                balloonContent: '$BALLOON_CONTENT$'
            }, {
                iconColor: '$ICON_COLOR$'
            });
            myMap.geoObjects.add(placemark);
            placemark.balloon.close();