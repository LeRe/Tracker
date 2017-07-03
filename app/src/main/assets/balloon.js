            var placemark = new ymaps.Placemark(myMap.getCenter(), {
                balloonContent: '$NICK_NAME$'
            }, {
                iconColor: '$ICON_COLOR$'
            });
            myMap.geoObjects.add(placemark);
            placemark.balloon.close();