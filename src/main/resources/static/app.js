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
            userId:''
        },
        image: '',
        coverImage: '',
        password: '',
        registerPassword: '',
        posts: [],
        showTextarea: false, // 是否顯示文章文字框
        comment: {
            userId: '',
            postId: '',
            content: '',
            createdAt:''
        },
        comments: [],
        showComments: false,
        biography:'',
    },
    methods: {
        register() {
            if (this.userName && this.registerEmail && this.registerPassword) {
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
            }
        },
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
        logout() {
            // 登出，移除user資料
            this.user = null;
        },
        editBiography() {
            Swal.fire({
                title: 'Edit Biography',
                input: 'textarea',
                inputValue: this.biography,
                showCancelButton: true,
                confirmButtonText: 'Save',
                showCloseButton: false, // 不顯示右下角關閉按鈕
            }).then((result) => {
                if (result.isConfirmed) {
                    this.biography = result.value; // 更新自我介紹
                    axios.post(`/api/v1/users/${this.user.userId}/biographies`, null, {
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
        handleImageUpload(event) {
            // 處理圖片上傳的事件
            this.image = event.target.files[0];
        },

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
                axios.post(`/api/v1/users/${userId}/images`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                })
                    .then(response => {
                        // Handle successful image upload
                        this.user.imgUrl = `/api/v1/users/${userId}/images` + '?rand=' + Math.random();
                        Swal.fire({
                            icon: 'success',
                            title: 'uploaded successfully',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    })
                    .catch(error => {
                        // Handle error
                        console.log("Error from AvatarImageUploader")
                    });
            });
        },
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
        fetchPosts() {
            axios.get('/api/v1/posts')
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
        showUserDetails(user) {
            Swal.fire({
                title: `${user.userName} Details`,
                html: `User ID: ${user.userId}<br>Biography: ${user.biography}`,
                showCancelButton: false,
                showConfirmButton: true
            });
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
                    }else {
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
                        <img src="${comment.user.imgUrl}" alt="User Avatar" class="user-avatar img-thumbnail"
                             @click="this.showUserDetails(comment.user)">
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
                            width: '80%', // 在這裡設定彈窗的寬度
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
