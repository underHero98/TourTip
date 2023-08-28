$(document).ready(function () {

    console.log("ready");
    // url 가져오기
    let urlSearch = new URLSearchParams(location.search);
    let mode = urlSearch.get("mode");
    let page = urlSearch.get("page");
    let postCnt = urlSearch.get("postCnt");
    let keyword = urlSearch.get("keyword");
    let searchType = urlSearch.get("searchType");

    <!--  버튼 클릭시 게시판 넘어가기 -->
    if (mode == 0) {
        console.log("모드0 공지사항");
        $(".Notice").show();
        $(".Qna").hide();
        $(".FreeBoard").hide();
        $('#btnNotice').css({"background":"#a2d9ff", "color":"white"});


    } else if (mode == 1) {
        console.log("모드1 QnA");
        $(".Notice").hide();
        $(".Qna").show();
        $(".FreeBoard").hide();
        $('#btnQna').css({"background":"#a2d9ff", "color":"white"});

    } else {
        console.log("모드2 게시판");
        $(".Notice").hide();
        $(".Qna").hide();
        $(".FreeBoard").show();
        $('#btnFreeBoard').css({"background":"#a2d9ff", "color":"white"});
    }


    function movePage(mode, page, postCnt, keyword, searchType) {
        let href = location.origin + location.pathname + "?";
        href += $.param({
            mode: mode,
            page: page,
            postCnt: postCnt,
            keyword: keyword,
            searchType: searchType
        });

        return href;
    }

    // 페이지 로딩이 되면 게시글 가져오기
    $.ajax({
        type: "GET",
        url: "getBoards",
        dataType: "json",
        data: {
            "mode": mode,
            "page": page,
            "postCnt": postCnt,
            "keyword": keyword,
            "searchType": searchType
        },
        success: function (boardInfo) {

            let boards = boardInfo["boards"];

            //페이지네이션
            const paginationContainer = document.querySelector('.pagination');
            const totalPages = boardInfo["totalPageNum"]; // 총 페이지 수
            const currentPage = Number(page); // 현재 페이지 번호
            const pageItemCount = Number(postCnt); // 한 페이지에 보여질  수

            // 이전 페이지 버튼
            const previousPageButton = document.createElement('li');
            previousPageButton.classList.add('page-item');
            previousPageButton.innerHTML = `<a class="page-link" href="#">Previous</a>`;
            paginationContainer.appendChild(previousPageButton);

            // 페이지 버튼
            for (let i = 1; i <= totalPages; i++) {
              const pageButton = document.createElement('li');
              pageButton.classList.add('page-item');
              pageButton.innerHTML =  `<a class="page-link" href="/page/board/?mode=${mode}&page=${i-1}&postCnt=${pageItemCount}#">${i}</a>`;
              if (i === currentPage+1) {
                pageButton.classList.add('active');
              }
              paginationContainer.appendChild(pageButton);
            }

            // 다음 페이지 버튼
            const nextPageButton = document.createElement('li');
            nextPageButton.classList.add('page-item');
            nextPageButton.innerHTML = `<a class="page-link" href="#">Next</a>`;
            paginationContainer.appendChild(nextPageButton);

            boards.forEach(board => {
                let divNumber = $('<div>').prop({class: "board-number", innerHTML: board.id});
                let divTitle = $('<div>').prop({class: "board-title", innerHTML: board.title});
                let divCreateAt = $('<div>').prop({class: "board-createAt", innerHTML: board.createAt});
                let divQna;
                let divLikeNum, divHit;

                if (mode == 1) {
                    // 문의 게시판의 경우 문의유형 포함
                    divQna = $('<div>').prop({class: "board-qna", innerHTML: board.qnaCategories});
                }
                if (mode == 2) {
                    // 자유 게시판의 경우 조회수 포함
                    divHit = $('<div>').prop({class: "board-hit", innerHTML: board.hitNum});
                }

                // pp 클래스로 묶어서 글 표시
                // 클래스 이름은 수정할 필요 있음
                $('<div class="pp">')
                    .append(divNumber)
                    .append(divTitle)
                    .append(divQna) // 문의게시판인 경우에만 포함
                    .append(divHit) // 자유게시판인 경우에만 포함
                    .append(divCreateAt)
                    .on('click', function() {
                        let href = location.origin;

                        href += "/page/board/board.html?";
                        href += $.param({ boardId: board.id });

                        location.href = href;
                    })
                    .appendTo('.board');
            });
        },
        error: function () {
            alert("server error!");
        }
    });

    if (mode == 0) {
        $('.bt_wrap2').appendTo('.Notice');
    } else if (mode == 2) {
        $('.bt_wrap2').appendTo('.FreeBoard');
    }

    // 공지의 10개의 글을 첫 페이지로 가져올 경우
    $('#btnNotice').on('click', function () {
        location.href = movePage(0, 0, 10, null, 0);
    });
    $('#btnQna').on('click', function () {
        location.href = movePage(1, 0, 10, null, 0);
    });
    $('#btnFreeBoard').on('click', function () {
        location.href = movePage(2, 0, 10, null, 0);
    });

    // 글 작성 이동
    $('.bt_wrap > .btn').on('click', function() {
        $.ajax({
            type: "POST",
            url: "moveWriteBoardPage",
            dataType: "text",
            data: {},
            success: function(returnPage) {

                let href = location.origin;
                href += returnPage + "?";
                href += $.param({ mode: mode });

                location.href = href;
            },
            error: function () {
                alert("session error : 로그인 후 이용해 주시기 바랍니다.");
                location.href = (location.origin + "/login.html");
            }
        })
    })
    $('img').on('click', function () {
        let key = $('#searchInput').val().toLowerCase();
        let type = $('#board-section').val();

        console.log(key);
        console.log(type);

        if ((key != "") && (key != null)) {
            location.href = movePage(mode, 0, 10, key, type);
        }
    });
});