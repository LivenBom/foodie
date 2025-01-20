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
                categoryId: null,  
                categoryName: '',
                columnId: null,    
                columnName: '',
                topicId: null,     
                topicTitle: '',
                isPaid: false
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
            loading: false,
            // 添加分类对话框数据
            categoryDialog: {
                visible: false,
                form: {
                    name: ''
                }
            },
            addCategoryDialogVisible: false,
            addColumnDialogVisible: false,
            newCategory: {
                name: ''
            },
            newColumn: {
                name: '',
                categoryId: null,  
                categoryName: '',
                icon: '',
                collapseEnable: 0  
            },
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
            return axios.get('/post/categories')
                .then(res => {
                    if (res.data.status === 200) {
                        this.categories = res.data.data;
                    } else {
                        this.$message.error(res.data.msg || '获取分类列表失败');
                        return Promise.reject(new Error('获取分类列表失败'));
                    }
                })
                .catch(err => {
                    console.error('获取分类列表失败:', err);
                    this.$message.error('获取分类列表失败，请重试');
                    return Promise.reject(err);
                });
        },
        handleCategoryChange(categoryId) {
            if (!categoryId) {
                // 重置相关字段
                this.postForm.columnId = null;
                this.postForm.columnName = '';
                this.postForm.topicId = null;
                this.postForm.topicTitle = '';
                this.columns = [];
                this.topics = [];
                return;
            }
            
            // 根据选中的分类ID获取对应的分类对象
            const category = this.categories.find(c => c.id === categoryId);
            if (category) {
                // 更新分类名称
                this.postForm.categoryName = category.title;  
                // 更新专栏列表
                this.columns = category.columnList || [];  
                
                // 重置专栏和主题相关字段
                this.postForm.columnId = null;
                this.postForm.columnName = '';
                this.postForm.topicId = null;
                this.postForm.topicTitle = '';
                this.topics = [];
            }
        },
        handleColumnChange(columnId) {
            // 重置主题相关字段
            this.postForm.topicId = null;
            this.postForm.topicTitle = '';
            this.topics = [];
            
            // 保存选中的专栏名称
            const column = this.columns.find(c => c.id === columnId);
            if (column) {
                this.postForm.columnName = column.title;  
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
                this.postForm.topicTitle = topic.title;  
            }
        },
        // 显示添加分类对话框
        showAddCategoryDialog() {
            this.categoryDialog.visible = true;
            this.categoryDialog.form.name = '';
        },
        // 提交新分类
        submitCategory() {
            if (!this.categoryDialog.form.name) {
                this.$message.warning('请输入分类名称');
                return;
            }
            
            axios.post('/post/category/create', {
                name: this.categoryDialog.form.name
            })
            .then(response => {
                if (response.data.status === 200) {
                    this.$message.success('添加分类成功');
                    this.categoryDialog.visible = false;
                    // 重新加载分类列表
                    this.loadCategories();
                } else {
                    this.$message.error(response.data.msg || '添加分类失败');
                }
            })
            .catch(error => {
                console.error('添加分类失败:', error);
                this.$message.error('添加分类失败');
            });
        },
        // 显示添加专栏对话框
        showAddColumnDialog() {
            if (!this.postForm.categoryId) {
                this.$message.warning('请先选择分类');
                return;
            }
            // 重置表单
            this.newColumn = {
                name: '',
                categoryId: this.postForm.categoryId,  
                categoryName: this.postForm.categoryName, 
                icon: '',
                collapseEnable: 0
            };
            this.addColumnDialogVisible = true;
        },

        // 提交新专栏
        submitNewColumn() {
            if (!this.newColumn.name) {
                this.$message.error('请输入专栏名称');
                return;
            }
            if (!this.newColumn.categoryId) {
                this.$message.error('请选择所属分类');
                return;
            }

            // 确保数据类型正确
            const submitData = {
                name: this.newColumn.name,
                categoryId: parseInt(this.newColumn.categoryId),
                icon: this.newColumn.icon || '',
                collapseEnable: parseInt(this.newColumn.collapseEnable)
            };

            // 发送请求创建新专栏
            axios.post('/post/column/create', submitData)
                .then(res => {
                    if (res.data.status === 200) {
                        this.$message.success('专栏创建成功');
                        this.addColumnDialogVisible = false;
                        
                        // 保存新创建的专栏名称和当前分类ID
                        const newColumnName = this.newColumn.name;
                        const currentCategoryId = this.postForm.categoryId;
                        
                        // 重置表单
                        this.newColumn = {
                            name: '',
                            categoryId: null,
                            categoryName: '',
                            icon: '',
                            collapseEnable: 0
                        };
                        
                        // 刷新分类列表
                        return this.loadCategories()
                            .then(() => {
                                // 重新触发分类选择，以刷新专栏列表
                                this.handleCategoryChange(currentCategoryId);
                                
                                // 在列表刷新后，找到新创建的专栏并选中它
                                const category = this.categories.find(c => c.id === currentCategoryId);
                                if (category && category.columnList) {
                                    const newColumn = category.columnList.find(col => col.title === newColumnName);
                                    if (newColumn) {
                                        // 选中新创建的专栏
                                        this.postForm.columnId = newColumn.id;
                                        this.postForm.columnName = newColumn.title;
                                        this.handleColumnChange(newColumn.id);
                                    }
                                }
                            })
                            .catch(err => {
                                console.error('刷新分类列表失败:', err);
                                this.$message.warning('专栏创建成功，但刷新列表失败，请手动刷新页面');
                            });
                    } else {
                        this.$message.error(res.data.msg || '创建失败');
                    }
                })
                .catch(err => {
                    console.error('创建专栏失败:', err);
                    this.$message.error('创建专栏失败，请重试');
                });
        },
        submitPost() {
            this.$refs.postForm.validate((valid) => {
                if (valid) {
                    this.loading = true;
                    
                    // 提交文章
                    const postData = {
                        title: this.postForm.title,
                        content: this.postForm.content,
                        topicId: this.postForm.topicId,
                        isPaid: this.postForm.isPaid ? 1 : 0
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
