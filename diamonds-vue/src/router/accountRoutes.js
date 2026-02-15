import { Layout, Items } from '@/pages/home';

export default {
    path: '/',
    component: Layout,
    children: [
        { path: 'items', component: Items },
    ]
};
