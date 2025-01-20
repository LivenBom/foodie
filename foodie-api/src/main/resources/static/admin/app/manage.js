// 配置 axios 请求拦截器
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('adminToken');
    if (token) {
        config.headers['Admin-Token'] = token;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 配置 axios 响应拦截器
axios.interceptors.response.use(response => {
    return response;
}, error => {
    if (error.response && error.response.status === 401) {
        // 如果返回 401，说明 token 已失效，跳转到登录页
        localStorage.removeItem('adminToken');
        localStorage.removeItem('adminUser');
        window.location.href = '/admin/login.html';
    }
    return Promise.reject(error);
});

new Vue({
    el: '#app',
    data() {
        return {
            // App列表
            appList: [],
            // 版本列表
            versionList: [],
            // 当前选中的App
            currentApp: null,
            // App对话框显示状态
            appDialogVisible: false,
            // 版本列表对话框显示状态
            versionListVisible: false,
            // 版本对话框显示状态
            versionDialogVisible: false,
            // 编辑中的App信息
            editingApp: {
                appName: '',
                appKey: '',
                packageName: '',
                platform: 2, // 默认iOS
                description: '',
                status: 1
            },
            // 新版本信息
            newVersion: {
                versionName: '',
                versionCode: 1,
                forceUpdate: 0,
                updateContent: '',
                downloadUrl: ''
            },
            // App表单验证规则
            appRules: {
                appName: [
                    { required: true, message: '请输入App名称', trigger: 'blur' },
                    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
                ],
                appKey: [
                    { required: true, message: '请输入App标识', trigger: 'blur' },
                    { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '只能包含字母、数字和下划线，且必须以字母开头', trigger: 'blur' }
                ],
                packageName: [
                    { required: true, message: '请输入包名', trigger: 'blur' },
                    { pattern: /^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)*$/, message: '请输入正确的包名格式', trigger: 'blur' }
                ],
                platform: [
                    { required: true, message: '请选择平台', trigger: 'change' }
                ]
            },
            // 版本表单验证规则
            versionRules: {
                versionName: [
                    { required: true, message: '请输入版本名称', trigger: 'blur' },
                    { pattern: /^\d+\.\d+\.\d+$/, message: '版本名称格式为：x.y.z', trigger: 'blur' }
                ],
                versionCode: [
                    { required: true, message: '请输入版本号', trigger: 'blur' },
                    { type: 'number', min: 1, message: '版本号必须大于0', trigger: 'blur' }
                ],
                updateContent: [
                    { required: true, message: '请输入更新内容', trigger: 'blur' }
                ]
            }
        }
    },
    created() {
        this.loadAppList();
    },
    methods: {
        // 加载App列表
        loadAppList() {
            axios.get('/app/list')
                .then(res => {
                    if (res.data.status === 200) {
                        this.appList = res.data.data;
                    } else {
                        this.$message.error(res.data.msg || '获取App列表失败');
                    }
                })
                .catch(err => {
                    console.error('获取App列表失败:', err);
                    this.$message.error('获取App列表失败，请重试');
                });
        },

        // 生成随机App标识
        generateAppKey() {
            const timestamp = Date.now().toString(36);
            const random = Math.random().toString(36).substring(2, 8);
            return 'app_' + timestamp + random;
        },
        // 显示添加App对话框
        showAddAppDialog() {
            this.editingApp = {
                appName: '',
                appKey: this.generateAppKey(),
                packageName: '',
                platform: 2, // 默认iOS
                description: '',
                status: 1
            };
            this.appDialogVisible = true;
        },

        // 显示编辑App对话框
        showEditAppDialog(app) {
            this.editingApp = JSON.parse(JSON.stringify(app));
            this.appDialogVisible = true;
        },

        // 提交App信息
        submitApp() {
            this.$refs.appForm.validate(valid => {
                if (valid) {
                    const url = this.editingApp.id ? '/app/update' : '/app/create';
                    axios.post(url, this.editingApp)
                        .then(res => {
                            if (res.data.status === 200) {
                                this.$message.success(this.editingApp.id ? 'App更新成功' : 'App创建成功');
                                this.appDialogVisible = false;
                                this.loadAppList();
                            } else {
                                this.$message.error(res.data.msg || (this.editingApp.id ? 'App更新失败' : 'App创建失败'));
                            }
                        })
                        .catch(err => {
                            console.error('提交App信息失败:', err);
                            this.$message.error('操作失败，请重试');
                        });
                }
            });
        },

        // 显示添加版本对话框
        showAddVersionDialog(app) {
            this.currentApp = app;
            this.newVersion = {
                appKey: app.appKey,
                versionName: '',
                versionCode: 1,
                forceUpdate: 0,
                updateContent: '',
                downloadUrl: ''
            };
            this.versionDialogVisible = true;
        },

        // 显示版本列表
        showVersionList(app) {
            this.currentApp = app;
            axios.get(`/app/version/list?appKey=${app.appKey}`)
                .then(res => {
                    if (res.data.status === 200) {
                        this.versionList = res.data.data;
                        this.versionListVisible = true;
                    } else {
                        this.$message.error(res.data.msg || '获取版本列表失败');
                    }
                })
                .catch(err => {
                    console.error('获取版本列表失败:', err);
                    this.$message.error('获取版本列表失败，请重试');
                });
        },

        // 提交版本信息
        submitVersion() {
            this.$refs.versionForm.validate(valid => {
                if (valid) {
                    axios.post('/app/version/create', this.newVersion)
                        .then(res => {
                            if (res.data.status === 200) {
                                this.$message.success('版本创建成功');
                                this.versionDialogVisible = false;
                                // 如果版本列表对话框是打开的，刷新版本列表
                                if (this.versionListVisible) {
                                    this.showVersionList(this.currentApp);
                                }
                            } else {
                                this.$message.error(res.data.msg || '版本创建失败');
                            }
                        })
                        .catch(err => {
                            console.error('创建版本失败:', err);
                            this.$message.error('创建版本失败，请重试');
                        });
                }
            });
        }
    }
});
