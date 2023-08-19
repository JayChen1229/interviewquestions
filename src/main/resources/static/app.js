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
        },
        image: '',
        coverImage: '',
        password: '',
        registerPassword: '',
        posts: [],
        showTextarea: false, // 是否顯示文章文字框
        comment: {
            user: '',
            post: '',
            content: ''
        },
        comments: [],
        showComments: false,
    },
    methods: {
        register() {
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
        login() {
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
                        this.user.imgUrl = '/img/users/' + this.user.userId;
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
        logout() {
            // 登出，移除user資料
            this.user = null;
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
                        const postId = response.data.postId; // Assuming the response contains the postId

                        // Upload image after post is created
                        this.uploadImage(postId);
                        this.image = null; // 清空已選擇照片
                        this.fetchPosts();  // 上傳成功 則刷新文章列表
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

        // 文字更新事件
        updateContent(event) {
            this.newPost.content = event.target.value;
        },
        handleImageUpload(event) {
            // 處理圖片上傳的事件
            this.image = event.target.files[0];
        },

        uploadImage(postId) {
            // 如果圖片存在
            if(this.image){
                const formData = new FormData();
                formData.append('image', this.image);
                axios.post(`/posts/${postId}/upload`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                })
                    .then(response => {
                        // 照片上傳後再次重置新文章的內容
                        this.fetchPosts();
                    })
                    .catch(error => {
                        // Handle error
                    });
            }
        },
        openImageUploader(userId) {
            Swal.fire({
                title: 'Upload Avatar',
                input: 'file',
                showCancelButton: true,
                confirmButtonText: 'Upload',
                cancelButtonText: 'Cancel',
                inputAttributes: {
                    accept: 'image/*'
                }
            }).then(result => {
                const formData = new FormData();
                formData.append('image', result.value);
                axios.post(`/users/${userId}/upload`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                })
                    .then(response => {
                        // Handle successful image upload
                        this.user.imgUrl = '/img/users/' + this.user.userId + '?rand=' + Math.random();
                        Swal.fire({
                            icon: 'success',
                            title: 'uploaded successfully',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    })
                    .catch(error => {
                        // Handle error
                    });
            });
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

        openImageModal(imageUrl) {
            // 使用 SweetAlert 來彈出大圖
            Swal.fire({
                imageUrl: imageUrl,
                imageAlt: 'Post Image',
                showConfirmButton: false,
                customClass: {
                    image: 'img-fluid'
                },
                width: '80%', // 設定寬度為螢幕的 80%
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
                    post.content = result.value;
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
