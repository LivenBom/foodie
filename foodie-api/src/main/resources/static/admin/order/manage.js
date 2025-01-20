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
        window.parent.location.href = '/admin/login.html';
    }
    return Promise.reject(error);
});

new Vue({
    el: '#app',
    data() {
        return {
            // 订单列表
            orders: [],
            // 分页信息
            pagination: {
                pageNum: 1,
                pageSize: 10,
                total: 0
            },
            // 搜索条件
            searchForm: {
                transactionId: '',
                productId: '',
                environment: ''
            },
            // 凭据详情
            receiptDialogVisible: false,
            selectedReceipt: null
        }
    },
    created() {
        this.loadOrderList();
    },
    methods: {
        // 加载订单列表
        loadOrderList() {
            const params = {
                page: this.pagination.pageNum,
                pageSize: this.pagination.pageSize,
                ...this.searchForm
            };

            axios.get('/order/list', { params })
                .then(res => {
                    if (res.data.status === 200) {
                        this.orders = res.data.data.list;
                        this.pagination.total = res.data.data.total;
                    } else {
                        this.$message.error(res.data.msg || '获取订单列表失败');
                    }
                })
                .catch(err => {
                    console.error('获取订单列表失败:', err);
                    this.$message.error('获取订单列表失败，请重试');
                });
        },

        // 处理页码变化
        handleCurrentChange(page) {
            this.pagination.pageNum = page;
            this.loadOrderList();
        },

        // 处理每页条数变化
        handleSizeChange(size) {
            this.pagination.pageSize = size;
            this.pagination.pageNum = 1;
            this.loadOrderList();
        },

        // 搜索订单
        searchOrders() {
            this.pagination.pageNum = 1;
            this.loadOrderList();
        },

        // 重置搜索条件
        resetSearch() {
            this.searchForm = {
                transactionId: '',
                productId: '',
                environment: ''
            };
            this.searchOrders();
        },

        // 查看凭据详情
        showReceiptDetails(order) {
            this.selectedReceipt = JSON.stringify(order, null, 2);
            this.receiptDialogVisible = true;
        }
    }
});
