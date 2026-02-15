import { createRouter, createWebHistory } from 'vue-router';

import accountRoutes from './accountRoutes'
import { useAuthStore, useAlertStore } from '@/stores';
import { Login } from '@/pages';

export const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    linkActiveClass: 'active',
    routes: [

        { path: '/sign-in', component: Login },

        { ...accountRoutes },
        
        // catch all redirect to home page
        // { path: '/:pathMatch(.*)*', redirect: '/' }

    ]
});

router.beforeEach(async (to) => {
    // clear alert on route change
    const alertStore = useAlertStore();
    alertStore.clear();

    // redirect to login page if not logged in and trying to access a restricted page 
    const publicPages = ['/sign-in'];

    const authRequired = !publicPages.includes(to.path);
    const authStore = useAuthStore();

    if (authRequired && !authStore.user) {
        authStore.returnUrl = to.fullPath;
        return '/sign-in';
    }
});


