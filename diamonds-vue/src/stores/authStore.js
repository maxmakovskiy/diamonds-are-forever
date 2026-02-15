import { defineStore } from 'pinia'
import { default as axios } from 'axios'

import { router } from '@/router'
import { useAlertStore } from '@/stores'


export const useAuthStore = defineStore("auth", {
    state: () => ({
        // initialize state from local storage to enable user to stay logged in
        user: JSON.parse(localStorage.getItem('user')),
        returnUrl: null
    }),

    actions: {
        async login(email, password) {
            try {
                // TODO:
                // What to save here?
                const resp = await axios.post("http://localhost:8080/sign-in", 
                  { email, password }, { withCredentials: true });    
                
                console.log("Auth store| login: " + resp);

                if (200 <= resp.status || resp.status < 300) {
                  // update pinia state
                  this.user = resp.data;
                  console.log(this.user)

                  // store user details and jwt in local storage to keep user logged in between page refreshes
                  localStorage.setItem('user', JSON.stringify(this.user));

                  // redirect to previous url or default to home page

                  if (this.returnUrl) {
                    router.push(this.returnUrl);
                  } else {
                    router.push('/');
                  }

                } else {
                  const alertStore = useAlertStore();
                  alertStore.error(error);                
                  console.log(error);
                }
            } catch (error) {
                const alertStore = useAlertStore();
                alertStore.error(error);                
                  console.log(error);
            }
        },

        async logout() {
            this.user = null;
            localStorage.removeItem('user');

            // TODO:
            // How to pass session id?
            try {
                await axios.post("http://localhost:8080/sign-out", { withCredentials: true });
            } catch (error) {
                const alertStore = useAlertStore();
                alertStore.error(error);
            }
            router.push('/sign-in');

        }
    }
});

