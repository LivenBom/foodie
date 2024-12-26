console.log('wangEditor:', window.wangEditor);

// 配置axios默认值
axios.defaults.baseURL = '';  
axios.defaults.headers.common['Content-Type'] = 'application/json';

// 添加请求拦截器，自动添加token
axios.interceptors.request.use(function (config) {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = token;
    }
    return config;
}, function (error) {
    return Promise.reject(error);
});

new Vue({
    el: '#app',
    data() {
        return {
            editor: null,
            postForm: {
                title: '',
                content: '',
                categoryId: '',
                categoryName: '',
                columnId: '',
                columnName: '',
                topicId: '',
                topicTitle: ''
            },
            rules: {
                title: [
                    { required: true, message: '请输入文章标题', trigger: 'blur' },
                    { min: 2, max: 100, message: '标题长度在2到100个字符', trigger: 'blur' }
                ],
                categoryId: [
                    { required: true, message: '请选择分类', trigger: 'change' }
                ],
                columnId: [
                    { required: true, message: '请选择专栏', trigger: 'change' }
                ],
                topicId: [
                    { required: true, message: '请选择主题', trigger: 'change' }
                ]
            },
            categories: [],
            columns: [],
            topics: [],
            loading: false
        }
    },
    mounted() {
        this.initEditor();
        this.loadCategories();
    },
    beforeDestroy() {
        if (this.editor) {
            this.editor.destroy();
        }
    },
    methods: {
        initEditor() {
            const editor = new window.wangEditor('#editor');
            
            // 配置编辑器
            editor.config.height = 500;
            editor.config.placeholder = '请输入内容...';
            
            // 配置 onchange 回调函数
            editor.config.onchange = (html) => {
                this.postForm.content = html;
            };

            // 创建编辑器
            editor.create();
            
            // 保存编辑器实例
            this.editor = editor;
        },
        loadCategories() {
            axios.get('/post/categories')
                .then(response => {
                    if (response.data.status === 200) {
                        this.categories = response.data.data;
                    } else {
                        this.$message.error(response.data.msg || '获取分类失败');
                    }
                })
                .catch(error => {
                    console.error('获取分类失败:', error);
                    this.$message.error('获取分类失败');
                });
        },
        handleCategoryChange(categoryId) {
            // 重置相关字段
            this.postForm.columnId = '';
            this.postForm.columnName = '';
            this.postForm.topicId = '';
            this.postForm.topicTitle = '';
            this.columns = [];
            this.topics = [];
            
            // 根据选择的分类获取专栏列表
            const category = this.categories.find(c => c.id === categoryId);
            if (category) {
                this.postForm.categoryName = category.name || category.title;
                this.columns = category.columns || category.columnList || [];
            }
        },
        handleColumnChange(columnId) {
            // 重置主题相关字段
            this.postForm.topicId = '';
            this.postForm.topicTitle = '';
            this.topics = [];
            
            // 保存选中的专栏名称
            const column = this.columns.find(c => c.id === columnId);
            if (column) {
                this.postForm.columnName = column.name || column.title;
            }
            
            // 获取主题列表
            if (columnId) {
                axios.get(`/post/topics?columnId=${columnId}`)
                    .then(response => {
                        if (response.data.status === 200) {
                            this.topics = response.data.data;
                        } else {
                            this.$message.error(response.data.msg || '获取主题失败');
                        }
                    })
                    .catch(error => {
                        console.error('获取主题失败:', error);
                        this.$message.error('获取主题失败');
                    });
            }
        },
        handleTopicChange(topicId) {
            // 保存选中的主题标题
            const topic = this.topics.find(t => t.id === topicId);
            if (topic) {
                this.postForm.topicTitle = topic.name || topic.title;
            }
        },
        submitPost() {
            this.$refs.postForm.validate((valid) => {
                if (valid) {
                    this.loading = true;
                    
                    // 提交文章
                    const postData = {
                        title: this.postForm.title,
                        content: this.postForm.content,
                        categoryId: this.postForm.categoryId,
                        categoryName: this.postForm.categoryName,
                        columnId: this.postForm.columnId,
                        columnName: this.postForm.columnName,
                        topicId: this.postForm.topicId,
                        topicTitle: this.postForm.topicTitle
                    };

                    axios.post('/post/write/create', postData)
                        .then(response => {
                            this.loading = false;
                            if (response.data.status === 200) {
                                this.$message.success('发布成功');
                                // 清空表单
                                this.$refs.postForm.resetFields();
                                this.editor.txt.html('<p><br></p>');
                            } else {
                                this.$message.error(response.data.msg || '发布失败');
                            }
                        })
                        .catch(error => {
                            this.loading = false;
                            console.error('发布失败:', error);
                            this.$message.error('发布失败');
                        });
                }
            });
        }
    }
});
