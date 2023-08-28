$(document).ready(function () {

    //취소 버튼 클릭시 login.html 이동
    $("#cancel").click(function() {
        location.href = "login"
    });

    //비밀번호 재확인
    $('#pw2').blur(function () {
        if ($('#pw').val() != $('#pw2').val()) {
            if ($('#pw2').val() != '') {
                //비밀번호가 공란이 아니고 일치하지도 않을 경우
                $("#check-result").html('비밀번호가 일치하지 않습니다..').css("color", "red");
                $(this).css("border", "2px solid red")
                $(this).val('');
                $(this).focus();
            }
        } else {
            //비밀번호가 일치할 경우
            $("#check-result").html('');
            $(this).css("border", "0px")
        }
    });

    $("#nickName").on("blur", function() {dupCheck(this, "#check-result2", "nickName")});
    $("#email").on("blur", function() {dupCheck(this, "#check-result3", "email")});


    function dupCheck(boxElem, pElem, name) {
        let boxE = $(boxElem);
        let pE = $(pElem);

        if (boxE.val() == "") {
            //공백일 경우
            boxE.css("border", "2px solid red")
            pE.html('필수입력란입니다.').css("color", "red");
        } else {
            $.get("userInfoCheck", { value : boxE.val(), name : name }, function(isExist) {
                if (isExist == false) {
                    //기존 유저 데이터와 겹치지 않는 경우
                    boxE.css("border", "0px");
                    pE.html('');
                } else {
                    //기존 유저 데이터와 겹치는 경우
                    if (name == "nickName") { pE.html('이미 존재하는 닉네임입니다.'); }
                    else if (name == "email") { pE.html('이미 존재하는 이메일입니다.'); }
                    boxE.css("border", "2px solid red")
                    pE.css("color", "red");
                }
            });
        }
    }
});
