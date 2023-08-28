const id = document.getElementById('id')
const password = document.getElementById('password')
const login = document.getElementById('login')
let errStack = 0;

login.addEventListener('click', () => {
    if (id.value == 'lee') {
        if (password.value == '0000') {
            alert('로그인 되었습니다!')
        }
        else {
            alert('아이디와 비밀번호를 다시 한 번 확인해주세요!')
            errStack ++;
        }
    }
    else {
        alert('존재하지 않는 사용자입니다.')
    }
})

$("#login").on("click",function(){

    let mb_id = $("#id").val();
    let mb_pwd = $("#password").val();

    if(mb_id == ""){
        alert("아이디를 입력해주세요.");
        

    }else if(mb_pwd == ""){
        alert("비밀번호를 입력해주세요");
    }
});


$(function(){
    $("#id").focus(function(){

        // $("#nickName").html('사용할 수 없는 아이디입니다.');
        // $("#nickName").attr('color','red');
    })
    $("#id").blur(function(){
        $(this).css("background-color","white")
    })
})