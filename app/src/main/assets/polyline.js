var myPolyline = new ymaps.Polyline(
    // Указываем координаты вершин ломаной.
    $POLYLINE_COORDINATES$
, {
    // Описываем свойства геообъекта.
    // Содержимое балуна.
    balloonContent: "Ломаная линия"
}, {
    // Задаем опции геообъекта.
    // Отключаем кнопку закрытия балуна.
    balloonCloseButton: false,
    // Цвет линии.
    strokeColor: "#000000",
    // Ширина линии.
    strokeWidth: 4,
    // Коэффициент прозрачности.
    strokeOpacity: 0.5
});
myMap.geoObjects.add(myPolyline);