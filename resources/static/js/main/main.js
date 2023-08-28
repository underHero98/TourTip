$(document).ready(function () {

    //체크인날짜 선택 이후 선택한 날짜 이전은 체크아웃 선택이 안되게 하는 것
    const checkinInput = document.getElementById("checkin");
    const checkoutInput = document.getElementById("checkout");

    checkinInput.addEventListener("input", function() {

    const checkinDate = new Date(checkinInput.value);
    const today = new Date(); //현재 날짜 

    console.log(today)

    if (checkinDate.getTime() < today.getTime()) {
        checkinInput.valueAsDate = today;
    }

    let minCheckoutDate = new Date(checkinInput.value);
    minCheckoutDate.setDate(minCheckoutDate.getDate() );

    const minCheckoutDateString = minCheckoutDate.toISOString().split("T")[0];
    checkoutInput.setAttribute("min", minCheckoutDateString);
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
        console.log("btn click!");
        var region1 = String(region);
        var checkIn1 = String(checkIn);
        var checkOut1 = String(checkOut);
        var capacity1 = parseInt(capacity);

        console.log(region + "!!");
        console.log(capacity);
        console.log("check in: " + checkIn);
        console.log("check out: " + checkOut);
        console.log("=========");
        console.log(region1);
        console.log(checkIn1);
        console.log(checkOut1);
        console.log(capacity1);

        $.ajax({
            url: "page/hotel/checkHotel",
            dataType: "json",
            type: "GET",
            data: {
                "region": region1,
                "checkIn": checkIn1,
                "checkOut": checkOut1,
                "capacity": capacity1
            },
            success: function (hotel) {
                if (hotel["hotelList"].length > 0) {
                    let href = location.origin;
                    href += "/page/hotel/hotelView?";
                    href += $.param({"region": region1, "checkIn": checkIn1, "checkOut": checkOut1, "capacity": capacity1});
                    location.href = href;
                } else {
                    alert("해당 검색 결과가 없습니다.");
                }
            },
            error: function () {
                alert("server error");
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


