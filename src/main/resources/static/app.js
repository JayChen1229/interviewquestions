new Vue({
    el: '#app',
    data: {
        userName: '',
        email: '',
        registerEmail: '',
        user: null,
        newPost: {            // 新文章的內容
            content: '',
            user: '',
            image: ''
        },
        coverImage: '',
        password: '',
        registerPassword: '',
        posts: [],
        showTextarea: false, // 是否顯示文章文字框
        comment:{
            user: '',
            post: '',
            content: ''
        },
        comments: [],
        showComments:false
        
    },
    methods: {
        register: function () {
            let user = {
                userName: this.userName,
                email: this.registerEmail,
                password: this.registerPassword,
            }
            axios.post('/register', user)
                .then(response => {
                    if (response.data) {
                        Swal.fire({
                            position: 'top',
                            title: 'Registration success',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                    } else {
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
        login: function () {
            axios.post('/login', null, {
                params: {
                    email: this.email,
                    password: this.password
                }
            })
                .then(response => {
                    if (response.data) {
                        // 登入成功，放入user資料
                        this.user = response.data;
                    } else {
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
            this.newPost.user = this.user;

            axios.post('/posts', this.newPost)
                .then(response => {
                    if (response) {
                        Swal.fire({
                            position: 'top',
                            title: 'Successfully published',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                        this.fetchPosts();
                    } else {
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
            axios.delete('/posts/' + postId)
                .then(response => {
                    if (response) {
                        Swal.fire({
                            position: 'top',
                            title: 'successfully deleted',
                            timer: 1500,
                            showConfirmButton: false,
                            background: 'rgba(255, 255, 255, .7)'
                        });
                        this.fetchPosts();
                    } else {
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
                });
        },

        formatDate(date) {
            const options = {
                year: 'numeric',
                month: 'short',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };
            const formatter = new Intl.DateTimeFormat(undefined, options);
            const formattedDate = formatter.format(new Date(date));
            return formattedDate;
        },
        editPost(post) {
            Swal.fire({
                title: 'Edit Content',
                input: 'textarea',
                inputValue: post.content,
                showCancelButton: true,
                confirmButtonText: 'Save',
                cancelButtonText: 'Cancel',
            }).then((result) => {
                if (result.isConfirmed) {
                    const editedContent = result.value;
                    // 在這裡你可以實現保存編輯內容的邏輯
                    console.log('Saving edited content:', editedContent);
                    post.content = editedContent;
                    axios.post('/posts', post)
                        .then(response => {
                            if (response) {
                                Swal.fire({
                                    position: 'top',
                                    title: 'Successfully modified',
                                    timer: 1500,
                                    showConfirmButton: false,
                                    background: 'rgba(255, 255, 255, .7)'
                                });
                                this.fetchPosts();
                            } else {
                                Swal.fire({
                                    position: 'top',
                                    title: 'fail to edit',
                                    timer: 1500,
                                    showConfirmButton: false,
                                    background: 'rgba(255, 255, 255, .7)'
                                });
                            }

                        });
                }
            });
        },
        // 添加留言
        addComment(post) {
            Swal.fire({
                title: 'Edit Content',
                input: 'textarea',
                // inputValue: this.comment.content,
                showCancelButton: true,
                confirmButtonText: 'Save',
                cancelButtonText: 'Cancel',
            }).then((result) => {
                if (result.isConfirmed) {
                    console.log(result.value);
                    this.comment.content = result.value;

                    // 在這裡你可以實現保存編輯內容的邏輯
                    this.comment.user = this.user;
                    this.comment.post = post;

                    console.log(this.comment);


        
                    axios.post('/comments', this.comment)
                        .then(response => {
                            if (response) {
                                Swal.fire({
                                    position: 'top',
                                    title: 'Successfully modified',
                                    timer: 1500,
                                    showConfirmButton: false,
                                    background: 'rgba(255, 255, 255, .7)'
                                });
                                this.fetchPosts();
                            } else {
                                Swal.fire({
                                    position: 'top',
                                    title: 'fail to edit',
                                    timer: 1500,
                                    showConfirmButton: false,
                                    background: 'rgba(255, 255, 255, .7)'
                                });
                            }

                        });
                }
            });
        },
        // 切換顯示/隱藏留言列表
        toggleComments(post) {
            axios.post('/comments/post', post)
                .then(response => {
                    if (response.data) {
                        this.comments = response.data;
                        const commentList = this.comments.map(comment => `${comment.user.userName}: ${comment.content}`).join('<br>');

                        Swal.fire({
                            title: 'Comments',
                            html: commentList,
                            showCancelButton: false,
                            showConfirmButton: true
                        });
                    } else {
                        console.log("fail");
                    }
                });
        }
    },
    created() {
        // 在 Vue 實例創建時獲取文章列表
        this.fetchPosts();
    }
});
