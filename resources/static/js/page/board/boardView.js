$(document).ready(function () {
    
    $(".comment:first-of-type").css("padding-top","50px")

    //게시글 아이디
    let boardId = new URLSearchParams(location.search).get("boardId");

    //댓글 작성 post 전달
    $("#comment_form").attr("action", "saveComment/" + boardId + "/0");

    //게시글 내용 가져오기
    $.get("getBoard", { boardId: boardId }, function (boardInfo) {

        let board = boardInfo['board'];
        let user = boardInfo['user'];

        $('.title').html(board.title);
        $('.cont > textarea').html(board.content);
        $('.info > dl:eq(0) > dd').html(user.nickName);
        $('.info > dl:eq(1) > dd').html(board.createAt);
        $('.info > dl:eq(2) > dd').html(board.hitNum);
        $('.info > dl:eq(3) > dd').html(board.likeNum);
    });

    //댓글 가져오기
    $.get("getComments/0", { boardId: boardId }, function (comments) {

        comments.forEach((comment, index) => {

            let commentDiv = $('<div>').appendTo('.comments').addClass("comment");
            $('<div>').html(comment.content).addClass("content").appendTo(commentDiv);
            addUserInfoToComment(comment, commentDiv);
            createRepliesDivFromComment(comment).insertAfter(commentDiv);
            createWriteReplyDiv(comment).insertAfter(commentDiv).hide();

            //토글 예시
            commentDiv.on('click', function() {
                $('.write-reply').eq(index).toggle();
            })
        });
    });

    //해당 댓글에 유저 정보 추가
    function addUserInfoToComment(comment, commentDiv) {
        $.get("getUserFromCommentId", { commentId: comment.id }, function (user) {
            $('<div>').html(user.nickName + " : ").addClass("nickName").prependTo(commentDiv);
            $('<div>').html(comment.createAt).addClass("createTime").appendTo(commentDiv);

            if (comment.parentsId == 0) {
                $('<div>').html("댓글 작성").addClass("reply-btn").appendTo(commentDiv)
            }
        });
    }

    //해당 댓글에 대댓글 정보 추가
    function createRepliesDivFromComment(comment) {

        //대댓글 영역 div 생성
        let repliesDiv = $('<div>').addClass("replies");

        $.get("getComments/" + comment.id, { boardId: boardId }, function (comments) {

            comments.forEach(c => {
                //대댓글 div 생성
                let commentDiv = $('<div>').appendTo(repliesDiv).addClass("reply");
                $('<div>').html(c.content).addClass("content").appendTo(commentDiv);
                addUserInfoToComment(c, commentDiv);
            });
        });

        return repliesDiv;
    }

    //작성 대댓글 div 요소 만들기
    function createWriteReplyDiv(comment) {

        //대댓글 div영역 생성
        let writeReplyDiv = $('<form>').addClass("write-reply").append(
            // 댓글 텍스트
            $('<input>').prop({
                "type": "text",
                "value": "댓글은 이쁘게 달아주세요~~",
                "name": "content"
            })
        ).append(
            // 버튼 작성
            $('<button>').prop({
                "type": "submit"
            }).html("저장")
        ).prop({
            // form 태그에 post 설정
            "method": "post",
            "action": "saveComment/" + boardId + "/" + comment.id //post전달 설정
        }).css("margin-bottom", "10px");

        return writeReplyDiv;
    }

    $('.changebtn').on('click', function(){
        location.href = 'change.html';
    })

    
});