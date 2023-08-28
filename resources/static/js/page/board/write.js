$(document).ready(function() {

    let urlSearch = new URLSearchParams(location.search);
    let mode = urlSearch.get("mode");

    if (mode == 2) {
        $('strong').text('자유게시판')
        $('.title').css("border-bottom", "1px dashed black");
        $('.title input[type="text"]').css("width","300px");
        $('.info').remove()
    }

    $(".bt_wrap > .on").on('click', function() {

        let title = $(".title > dl > dd > input[type='text']").val();
        let content = $(".cont > textarea").val();
        let qnaCategories = "";

        if (mode == 1) {
            qnaCategories = $('.info > dl > select').val();
        }

        $.ajax({
            url: "registBoard",
            dataType: "text",
            data: { mode: mode, content: content, title: title, qnaCategories: qnaCategories },
            type: "POST",
            success: function(href) {
                console.log(location.origin + href);
                location.replace(location.origin + href);
            },
            error: function() {
                alert("server error occured");
            }
        });
    });
});