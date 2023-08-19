new Vue({
    el: '#app',
    data: {
        userName: '',
        email: '',
        registerEmail: '',
        user: null,
        newPost: {            // 新文章的內容
            content: '',
            user:'',
            image:''
        },
        coverImage:'',
        password:'',
        registerPassword:'',
        posts: [],
        showTextarea: false // 是否顯示文章文字框
    },
    methods: {
        register: function() {
            let user={
                userName:this.userName,
                email:this.registerEmail,
                password:this.registerPassword,
            }
            axios.post('/register', user)
                 .then(response => {
                    if(response.data){
                        console.log(response.data);
                        Swal.fire({
                            position: 'top',
                            title: 'Registration success',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                          });
                    }else{
                        console.log(response.data);
                        Swal.fire({
                            
                            position: 'top',
                            title: 'This mailbox is already registered',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                          });
                    }
                   
                 
                 });
        },
        login: function() {
            axios.post('/login', null, {
                params: {
                    email: this.email,
                    password: this.password
                }
            })
                .then(response => {
                    if(response.data){
                        // 登入成功，放入user資料
                        this.user = response.data;

                        // test
                        console.log(this.user.userId)
                    }else{
                        Swal.fire({
                            position: 'top',
                            title: 'Login failed',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                    }

                });
        },
        postPost() {


            console.log(this.user);
            this.newPost.user = this.user;

            axios.post('/posts', this.newPost)
                .then(response => {
                    if(response){
                        Swal.fire({
                            position: 'top',
                            title: 'Successfully published',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                        this.fetchPosts();
                    }else{
                        Swal.fire({
                            position: 'top',
                            title: 'Failed to publish',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                    }

                });

            // 重置新文章的內容並隱藏文章文字框
            this.newPost.content = '';
            this.showTextarea = false;
        },
        deletePost(postId) {
            axios.delete('/posts/'+postId)
                .then(response => {
                    if(response){
                        Swal.fire({
                            position: 'top',
                            title: 'successfully deleted',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                        this.fetchPosts();
                    }else{
                        Swal.fire({
                            position: 'top',
                            title: 'failed to delete',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                    }
                });
        },
        fetchPosts() {
            axios.get('/posts')
                .then(response => {
                    this.posts = response.data;
                    console.log(this.posts);
                });
        },

        formatDate(date) {
            const options = { year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' };
            const formatter = new Intl.DateTimeFormat(undefined, options);
            const formattedDate = formatter.format(new Date(date));
            console.log(formattedDate);
            return formattedDate;
        }

        

    },
    created() {
        // 在 Vue 實例創建時獲取文章列表
        this.fetchPosts();
    }
});
