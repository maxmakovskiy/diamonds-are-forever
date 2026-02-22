import { createRouter, createWebHistory } from 'vue-router';

import items from './items'
import actions from './actions'
import profile from './profile'
import { Login } from '@/pages'

import { useAuthStore, useAlertStore } from '@/stores'

export const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    linkActiveClass: 'active',
    routes: [

        { path: '/sign-in', component: Login },

        { ...items },
        { ...actions },
        { ...profile },

        
        // catch all redirect to home page
        { path: '/:pathMatch(.*)*', redirect: '/items' }

    ]
});

/*
// TODO: temp comment
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
*/

