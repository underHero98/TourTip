$(document).ready(function () {

    let region = new URLSearchParams(location.search).get("region");
    let checkIn = new URLSearchParams(location.search).get("checkIn");
    let checkOut = new URLSearchParams(location.search).get("checkOut");
    let capacity = new URLSearchParams(location.search).get("capacity");

    //지역 띄우기
    let regionResult = $('.location');
    regionResult.text(region.toUpperCase());

    //날짜 띄우기
    $("#checkin").attr("value", checkIn);
    $("#checkout").attr("value", checkOut);

    //인원수 띄우기
    let capacityResult = $('.people');
    capacityResult.text('성인' + capacity + '명');
    capacityResult.css('visibility', 'visible');

    $.ajax({
        url: "checkHotel",
        dataType: "json",
        type: "GET",
        data: {
            "region": region,
            "checkIn": checkIn,
            "checkOut": checkOut,
            "capacity": capacity
        },
        success: function (hotel) {

            let hotelList = hotel["hotelList"];
            let hotelImgList = hotel["hotelImgList"];

            hotelList.forEach((h, index) => {
                let info =
                    "<div class='hotel_info'>"
                    + "<div class='hotel_img'>" + "</div>"
                    + "<div class='info1'>" + h.name + "</div>"
                    + "<div class='info2'>"
                    + "<span class='star'>" + "★".repeat(h.starRating) + "</span>"
                    + "<span class='count'>" + h.starRating + "</span>"
                    + "</div>"
                    + "<div class='info3'>"
                    + "<span class='price'>" + h.price.toLocaleString("ko-KR") + "원" + "</span>"
                    + "</div>" + "</div>";

                $("<div class='hotel'>").html(info).appendTo(".hotel_box").on("click", function () {
                    let href = location.origin;
                    // href += "/page/hotel/hotelDetail?hotelId=";
                    // href += h.id;
                    href += "/page/hotel/hotelDetail?";
                    href += $.param({"hotelId": h.id, "checkInDate": h.checkIn});

                    location.href = href;
                });
            });

            hotelImgList.forEach((img, index) => {
                $(".hotel_img").eq(index)
                    .css("background-image", "url("+img+")")
                    .css("background-size", "cover");
            })
        }


    });


    // 클릭시 지역이 버튼에 바뀌게하기
    const resultDiv = $('.location');
    const citiesBtns = $('.Cities_button button');

    citiesBtns.each(function () {
        $(this).on('click', function () {
            resultDiv.text($(this).text().toUpperCase());
            this.value = resultDiv.text($(this).text().toUpperCase()).text();
            region = this.value;
        });

    });

    // 체크인/아웃 날짜
    $("input[name=checkin]").change(function () {
        this.checkin = $(this).val();
        checkIn = this.checkin;
    });
    $("input[name=checkout]").change(function () {
        this.checkout = $(this).val();
        checkOut = this.checkout;
    });

    //ajax
    $("#search_hotel").click(function () {
        $("div[class=hotel]").remove();
        console.log("btn click!");
        var region1 = String(region);
        var checkIn1 = String(checkIn);
        var checkOut1 = String(checkOut);
        var capacity1 = parseInt(capacity);

        console.log("=========");
        console.log(region1);
        console.log(checkIn1);
        console.log(checkOut1);
        console.log(capacity1);

        $.ajax({
            url: "checkHotel",
            dataType: "json",
            type: "GET",
            data: {
                "region": region1,
                "checkIn": checkIn1,
                "checkOut": checkOut1,
                "capacity": capacity1
            },
            success: function (hotel) {
                hotel.forEach(h => {

                    var info =
                        "<div class=\"hotel_info\">"
                        + "<div class='hotel_img'>" + "</div>"
                        + "<div class=\"info1\">" + h.name + "</div>"
                        + "<div class=\"info2\">"
                        + "<span class='star'>" + "★".repeat(h.starRating) + "</span>"
                        + "<span class=\"count\">" + h.starRating + "</span>"
                        + "</div>"
                        + "<div class=\"info3\">"
                        + "<span class=\"price\">" + h.price + "</span>"
                        + "</div>" + "</div>";

                    $("<div class='hotel'>").html(info).appendTo(".hotel_box").on("click", function () {
                        let href = location.origin;
                        href += "/page/hotel/hotelDetail?";
                        href += $.param({"hotelId": h.id, "checkInDate": h.checkIn});
                        location.href = href;
                    });


                });
            }


        });

    });

});

//인원 수 관련

let capacity;
let checkIn;
let checkOut;

function count(type) {
    const resultDiv = $('#adult_result');
    let count2 = parseInt(resultDiv.text());

    if (type === 'minus' && count2 > 0) {
        count2--;
    } else if (type === 'plus') {
        count2++;
    }
    resultDiv.text(count2);
    this.capacity2 = resultDiv.text(count2).text();
    capacity = this.capacity2;
}

function showResult() {
    const resultDiv = $('#adult_result');
    const count = parseInt(resultDiv.text());
    const peopleDiv = $('.people');
    peopleDiv.text(`성인 ${count}명`);
    peopleDiv.css('visibility', 'visible');
}


