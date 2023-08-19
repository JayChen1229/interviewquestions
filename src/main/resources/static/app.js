new Vue({
    el: '#app',
    data: {
        userName: '',
        email: '',
        registerEmail: '',
        user: null,
        newPost: '',
        coverImage:'',
        password:'',
        registerPassword:'',
        posts: []
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
        addPost: function() {
            axios.post('/api/posts', { content: this.newPost })
                 .then(response => {
                     this.posts.unshift(response.data);
                     this.newPost = '';
                 });
        },
        deletePost: function(postId) {
            axios.delete('/api/posts/' + postId)
                 .then(() => {
                     this.posts = this.posts.filter(post => post.id !== postId);
                 });
        }
    },
    created: function() {
        axios.get('/api/posts')
             .then(response => {
                 this.posts = response.data;
             });
    }
});
