new Vue({
    el: '#app',
    data: {
        userName: '',
        password: '',
        email: '',
        biography: '',

        registerEmail: '',
        registerPassword: '',

        newPost: {
            content: '',
            user: '',
            userId: ''
        },
        comment: {
            userId: '',
            postId: '',
            content: '',
            createdAt: ''
        },

        image: '',
        coverImage: '',

        posts: [],
        comments: [],

        user: null,
        showTextarea: false,
        showComments: false,

    },
    methods: {
        // 註冊功能
        register() {
            // 設定正則表達式
            const userNameRegex = /^[a-zA-Z0-9_-]{3,16}$/;
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;

            if (userNameRegex.test(this.userName) && emailRegex.test(this.registerEmail) && passwordRegex.test(this.registerPassword)) {
                let user = {
                    userName: this.userName,
                    email: this.registerEmail,
                    password: this.registerPassword,
                };
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
            } else {
                Swal.fire({
                    position: 'top',
                    title: 'Invalid input',
                    timer: 1500,
                    showConfirmButton: false,
                    background: 'rgba(255, 255, 255, .7)'
                });
            }
        },

        // 登入功能
        login() {
            if (this.email && this.password) {
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
                            this.user.imgUrl = `/api/v1/users/${this.user.userId}/images`;
                            this.biography = this.user.biography;
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
            }
        },
        // 登出功能
        logout() {
            this.user = null;
        },
        // 預設頭像
        useDefaultImage(event) {
            event.target.src = "defaultAvatar.png";
        },
        // 修改自介
        editBiography() {
            Swal.fire({
                title: 'Edit Biography',
                input: 'textarea',
                inputValue: this.biography,
                showCancelButton: true,
                confirmButtonText: 'Save',
                showCloseButton: false,
            }).then((result) => {
                if (result.isConfirmed) {
                    this.biography = result.value; // 更新自我介紹
                    axios.put(`/api/v1/users/${this.user.userId}/biographies`, null, {
                        params: {
                            biography: this.biography
                        }
                    })
                        .then(response => {
                            if (response) {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Biography Update Success',
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                            } else {
                                Swal.fire({
                                    icon: 'warning',
                                    title: 'Biography Update failed',
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                            }
                        });
                }
            });
        },
        // 發布文章
        postPost() {
            if (this.newPost.content && this.user.userId) {
                this.newPost.user = this.user;
                this.newPost.userId = this.user.userId;
                axios.post('/api/v1/posts', this.newPost)
                    .then(response => {
                        if (response) {
                            Swal.fire({
                                position: 'top',
                                title: 'Successfully published',
                                timer: 1500,
                                showConfirmButton: false,
                                background: 'rgba(255, 255, 255, .7)'
                            });
                            const postId = response.data.postId;
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
            } else {
                Swal.fire({
                    icon: 'warning',
                    title: 'Post content cannot be empty',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
            // 重置新文章的內容並隱藏文章文字框
            this.newPost.content = '';
            this.showTextarea = false;
        },
        // 文字更新事件
        updateContent(event) {
            this.newPost.content = event.target.value;
        },
        // 處理圖片上傳的事件
        handleImageUpload(event) {
            this.image = event.target.files[0];
        },
        // 上傳文章圖片
        uploadImage(postId) {
            // 如果圖片存在
            if (this.image) {
                const formData = new FormData();
                formData.append('image', this.image);
                axios.post(`/api/v1/posts/${postId}/images`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                })
                    .then(response => {
                        // 照片上傳後再次重置新文章的內容
                        this.fetchPosts();
                    })
                    .catch(error => {
                        console.log("Error from uploadImage")
                    });
            }
        },
        // 上傳會員圖片
        openImageUploader(userId) {
            Swal.fire({
                title: 'Upload Avatar',
                input: 'file',
                showCancelButton: true,
                confirmButtonText: 'Upload',
                cancelButtonText: 'Cancel',
                inputAttributes: {
                    accept: 'image/*'
                },
                html: '<p style="font-size: 14px; color: gray;">File size must not exceed 10MB.</p>',
            }).then(result => {
                if (result.isConfirmed) {  // 檢查使用者是否點擊上傳按鈕
                    const formData = new FormData();
                    formData.append('image', result.value);

                    axios.post(`/api/v1/users/${userId}/images`, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data',
                        },
                    })
                        .then(response => {
                            this.user.imgUrl = `/api/v1/users/${userId}/images` + '?rand=' + Math.random();
                            Swal.fire({
                                icon: 'success',
                                title: 'Uploaded successfully',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        })
                        .catch(error => {
                            console.error("Error from AvatarImageUploader", error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Upload failed',
                                text: 'There was a problem uploading your image. Please try again.'
                            });
                        });
                }
            });
        },
        // 刪除文章
        deletePost(postId) {
            axios.delete(`/api/v1/posts/${postId}`)
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
        // 獲取所有文章
        fetchPosts() {
            axios.get('/api/v1/posts')
                .then(response => {
                    this.posts = response.data;
                });
        },
        // 修改文章
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
                    axios.post('/api/v1/posts', post)
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
        // 放大文章圖片
        openImageModal(imageUrl) {
            // 使用 SweetAlert 來彈出大圖
            Swal.fire({
                imageUrl: imageUrl,
                imageAlt: 'Post Image',
                showConfirmButton: false,
                showCloseButton: true,
                customClass: {
                    image: 'img-fluid'
                },
                width: '80%',
            });
        },
        // 設定時間格式
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
        // 添加留言
        addComment(post) {
            Swal.fire({
                title: 'Add Comment',
                input: 'textarea',
                showCancelButton: true,
                confirmButtonText: 'Save',
                cancelButtonText: 'Cancel',
            }).then((result) => {
                if (result.isConfirmed) {
                    // 避免文字框為null
                    if (result.value) {
                        this.comment.content = result.value;
                        console.log(result.value)
                        this.comment.userId = this.user.userId;
                        this.comment.postId = post.postId;

                        axios.post('api/v1/comments', this.comment)
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
                    } else {
                        Swal.fire({
                            icon: 'warning',
                            title: 'Comment cannot be empty',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    }
                }
            });
        },
        // 切換顯示/隱藏留言列表
        toggleComments(post) {
            axios.get(`/api/v1/posts/${post.postId}/comments`)
                .then(response => {
                    if (response.data) {
                        this.comments = response.data;
                        const commentTable = `
    <table class="table">
        <thead>
            <tr>
                <th>User</th>
                <th>Comment</th>
                <th>Time</th>
            </tr>
        </thead>
        <tbody>
            ${this.comments.map(comment => `
                <tr>
                    <td>
                        <img src="${comment.user.imgUrl}" alt="User Avatar" class="user-avatar img-thumbnail">
                        ${comment.user.userName}
                    </td>
                    <td>${comment.content}</td>
                    <td>${this.formatDate(comment.createdAt)}</td>
                </tr>
            `).join('')}
        </tbody>
    </table>
`;
                        Swal.fire({
                            title: 'Comments',
                            html: commentTable,
                            width: '80%',
                            showCloseButton: true,
                        });
                    } else {
                        console.log("fail");
                    }
                });
        }

    },
    // 設定正則表達式
    computed: {
        isValidUserName() {
            const userNameRegex = /^[a-zA-Z0-9_-]{3,16}$/;
            return this.userName && userNameRegex.test(this.userName);
        },
        isValidEmail() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return this.registerEmail && emailRegex.test(this.registerEmail);
        },
        isValidPassword() {
            const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
            return this.registerPassword && passwordRegex.test(this.registerPassword);
        }
    },
    // 在 Vue 實例創建時獲取文章列表
    created() {
        this.fetchPosts();
    }
});
