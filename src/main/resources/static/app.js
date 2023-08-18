new Vue({
    el: '#app',
    data: {
        userName: '',
        email: '',
        user: null,
        newPost: '',
        coverImage:'',
        password:'',
        posts: []
    },
    methods: {
        register: function() {
            let user={
                userName:this.userName,
                email:this.email,
                password:this.password,
            }
            axios.post('/users', user)
                 .then(response => {
                     this.user = response.data;
                 });
        },
        login: function() {
            axios.post('/api/login', { phone: this.phone })
                 .then(response => {
                     this.user = response.data;
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
