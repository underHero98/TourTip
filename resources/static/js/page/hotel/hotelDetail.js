$(document).ready(function () {

    let hotelId = new URLSearchParams(location.search).get("hotelId");
    let checkInDate = new URLSearchParams(location.search).get("checkInDate");

    // 후기 더보기 버튼을 누를 시
    $(".review_box").slice(0, 1).show();
    $("#load").click(function (e) { // 클릭시 more
        console.log("dd");
        e.preventDefault();
        $(".review_box:hidden").slice(0, 1).show();
        if ($(".review_box:hidden").length == 0) {
            $("#load").css("display", "none")
        }
    });

    // 근처 추천 장소(관광/맛집) 버튼 클릭시
    $("#tourlocation").click(function () {
        console.log("ss");
        $(".tourlocation").slice(0, 2).show();
        $(".restaurant").hide();
        $("#load2").show();
    });

    $("#restaurant").click(function () {
        console.log("ss");
        $(".restaurant").slice(0, 2).show();
        $(".tourlocation").hide();
        $("#load2").show();

    });

    // 처음에는 .tourlocation 클래스를 가진 요소 중 처음 2개만 보이도록 설정
    $(".tourlocation").slice(0, 2).show();
    $(".restaurant").slice(0, 2).hide();
    $("#load2").show();

    // 더 많은 장소 버튼 클릭시
    $("#load2").click(function (e) {
        e.preventDefault();
        // 탭에 따라서 .tourlocation .restaurant만 보여준다.
        if ($(".tourlocation:visible").length > 0) {
            $(".tourlocation:hidden").slice(0, 2).show();
            if ($(".tourlocation:hidden").length == 0) {
                $("#load2").css("display", "none");
            }

        } else if ($(".restaurant:visible").length > 0) {
            $(".restaurant:hidden").slice(0, 2).show();
            if ($(".restaurant:hidden").length == 0) {
                $("#load2").css("display", "none");
            }
        }
    });

    // 리뷰 작성시 필요한 쿼리 추가
    $(".hidden-hotel-id").val(hotelId);
    $(".hidden-check-in-date").val(checkInDate);

    // 호텔 상세 정보 주입
    $.ajax({
        url: "hotelDetailInfo",
        dataType: "json",
        data: {hotelId: hotelId, checkInDate: checkInDate},
        type: "GET",
        success: function (hotelDetailInfo) {

            // 호텔 정보
            let hotel = hotelDetailInfo["hotel"];
            let hotelImg = hotelDetailInfo["imgUrlListH"];

            $(".detail > h3").html(hotel.name);
            $(".price").text(hotel.price.toLocaleString("ko-KR") + "원");
            $(".star").text("★" + hotel.starRating);

            // 호텔 대표 이미지 3개 삽입
            for(let i = 0; i < hotelImg.length; i++) {
                let item = $("<div class='carousel-item'>").appendTo(".carousel-inner").each(function() {
                        if (i == 0) { $(this).addClass("active"); }
                    });
                $("<img src="+hotelImg[i]+" class='d-block w-100' alt='...'>").appendTo(item);
            }

            // 날씨 정보
            let weatherInfoList = hotelDetailInfo["weatherInfoList"];

            for(let i = 0; i < weatherInfoList.length; i++) {

                let date = new Date(checkInDate);
                date = date.addDays(i);

                let dateStr = String(date.getMonth()+1) + "." + String(date.getDate());
                let weatherInfo = weatherInfoList[i];

                $("<li class='weather_box_list_item'>").appendTo($(".weather_box_list"))
                    .append($("<div class='day'>").html(dateStr))
                    .append($("<div class='line_bar'>"))
                    .append($("<div class='left'>").css("background-image", "url("+weatherInfo["icon"]+")"))
                    .append($("<div class='right'>"))
                    .append($("<span class='temperature_inner'>")
                        .append($("<span class='lowest'>").html(weatherInfo["min"]))
                        .append($("<span class='bar'>").html("/"))
                        .append($("<span class='highest'>").html(weatherInfo["max"])));

            }

            // 주소 보여주기, 옆에버튼 클립보드 복사
            $(".adress_detail").text(hotel.addr);

            //====================================================================

            // 리뷰 정보
            let reviewList = hotelDetailInfo["reviewList"];
            let reviewNickNameList = hotelDetailInfo["reviewNickNameList"];

            // 작성된 리뷰 정보
            for(let i = 0; i < reviewList.length; i++) {

                // review box 생성
                if (i % 2 == 0) {
                    $("<div class='review_box'>").insertBefore("#load").each(function() {
                        $(this).css("display", (i == 0) ? "block" : "none");
                    });
                }

                // review box item 생성
                $("<div>").addClass((i % 2 == 0) ? "review_box_left" : "review_box_right")
                    .append(createReviewBoxItem(reviewNickNameList[i], reviewList[i]))
                    .appendTo($(".review_box").last());
            }

            //====================================================================

            //맛집
            let foodImg = hotelDetailInfo['imgUrlListF'];
            let food = hotelDetailInfo['foodList'];
            let json = JSON.stringify(food);
            let obj = JSON.parse(json);

            //관광지
            let viewImg = hotelDetailInfo['imgUrlListV'];
            let view = hotelDetailInfo['viewList'];
            let json2 = JSON.stringify(view);
            let viewObj = JSON.parse(json2);

            //호탤 중심좌표
            let posX = hotel.posX;
            let posY = hotel.posY;

            let mapContainer = document.getElementById('map'), // 지도를 표시할 div
                mapOption = {
                    center: new kakao.maps.LatLng(posX, posY), // 지도의 중심좌표
                    level: 1 // 지도의 확대 레벨
                };

            let map = new kakao.maps.Map(mapContainer, mapOption);              // 지도 생성
            let markerPosition = new kakao.maps.LatLng(posX, posY);             // 마커가 표시되는 위치
            let marker = new kakao.maps.Marker({ position: markerPosition });   // 마커 생성

            // 마커가 지도 위에 표시되도록 설정합니다
            marker.setMap(map);

            // 인포윈도우를 생성합니다
            let infowindow = new kakao.maps.InfoWindow({
                content: "<div style='position: absolute; left: 0px; top: 0px;'><div style='width:140px;padding:1px;text-align:center;'>숙소</div></div>"
            });

            // 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
            // infowindow.open(map, marker);

            let imageSrc = 'https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678111-map-marker-48.png';       // 마커이미지 주소
            let imageSrc2 = '../../../image/marker.png';                                                            // 마커이미지 주소
            
            let imageSize = new kakao.maps.Size(34, 36);                                        // 마커 이미지의 크기
            let imageOption = {offset: new kakao.maps.Point(17, 36)};                           // 마커의 좌표와 일치시킬 이미지 안에서의 좌표설정

            // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
            let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
            let markerImage2 = new kakao.maps.MarkerImage(imageSrc2, imageSize, imageOption);

            // 관광지 마커 추가
            for (let i = 0; i < viewObj.length; i++) {
                addSubMarkerOnMap(viewObj[i], markerImage2, map, 'customoverlay2');
                if (i < 8) {
                    let subFoodRecommendDiv = addSubContent(viewObj[i], "tourlocation", viewImg[i]);
                    if (i < 2)  {
                        subFoodRecommendDiv.css("display", "block");
                    }
                }
            }

            // 맛집 마커 추가
            for (let i = 0; i < obj.length; i++) {
                addSubMarkerOnMap(obj[i], markerImage, map, 'customoverlay');
                if (i < 8) {  addSubContent(obj[i], "restaurant", foodImg[i]); }
            }

            // 지도 타입 변경 컨트롤을 생성 후 지도의 상단 우측에 지도 타입 변경 컨트롤 추가
            // 지도에 확대 축소 컨트롤을 생성 후 지도의 우측에 확대 축소 컨트롤 추가
            map.addControl(new kakao.maps.MapTypeControl(), kakao.maps.ControlPosition.TOPRIGHT);
            map.addControl(new kakao.maps.ZoomControl(), kakao.maps.ControlPosition.RIGHT);

            // 아래 코드는 지도 위의 마커를 제거하는 코드입니다
            // marker.setMap(null);
            //====================================================================
        },
        error: function () {
            alert("server error");
        }
    });

    // 주소 클립보드로 복사
    new ClipboardJS($('.location_adress .copy').get(0)).on('success', function (e) {console.log(e);});
});

// 지도에 추가할 '근처 추천장소' 마커 추가
function addSubMarkerOnMap(sub, markerImage, map, divClassName) {
    new daum.maps.Marker({
        position: new daum.maps.LatLng(sub.posX, sub.posY),
        title: sub.name,
        image: markerImage,
        map: map
    });

    let contentStr = "<div class="+divClassName+"><a href='https://map.kakao.com/link/map/" + sub.addr + "' target='_blank'><span class='title'>" + sub.name + "</span></a></div>";

    new kakao.maps.CustomOverlay({
        map: map,
        position: new daum.maps.LatLng(sub.posX, sub.posY),
        content: contentStr,
        yAnchor: 1
    });
}

// 근처 추천장소 div 추가
function addSubContent(sub, className, strImg) {

    // Content Div
    let subContentDiv = $("<div>").addClass("content").addClass(className).css("display", "none");

    // Left Div
    let subContentLeftDiv = $("<div>").addClass("content_left").appendTo(subContentDiv);
    $("<h3>").html(sub["name"]).appendTo(subContentLeftDiv);
    $("<p>").html(sub["note"]).appendTo(subContentLeftDiv);

    // Right Div
    let subContentRightDiv = $("<div>").addClass("content_right").appendTo(subContentDiv);
    $("<div>").addClass("img")
        .css("background", "url("+strImg+")")
        .css("background-size", "cover").appendTo(subContentRightDiv);

    subContentDiv.insertBefore($("#load2"));

    return subContentDiv;
}

function createReviewBoxItem(userNickName, review) {

    return $("<div class='review_box_item'>")
        .append($("<div class='review_box_item_id'>").html(userNickName))
        .append($("<div class='review_box_item_star'>").each(function() {
            let point = review["reviewPoint"];
            let value = "";

            value += "★".repeat(point);
            value += "☆".repeat(5-point);

            $(this).html(value);
        }))
        .append($("<div class='review_box_item_writeDay'>").html(review["createAt"]))
        .append($("<div class='review_box_item_contents'>").html(review["content"]));
}

Date.prototype.addDays = function(days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
}