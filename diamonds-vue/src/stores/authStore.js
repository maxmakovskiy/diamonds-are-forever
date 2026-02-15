import { defineStore } from 'pinia'
import { default as axios } from 'axios'

import { router } from '@/router'
import { useAlertStore } from '@/stores'


export const useAuthStore = defineStore("auth", {
    state: () => ({
        // NOTE:
        // Based on availability of the cookie (session-id)
        // we intialize user by calling GET on /profile 
        user: null,
        returnUrl: null
    }),

    actions: {
        async login(email, password) {
            try {
                const resp = await axios.post("http://localhost:8080/sign-in", 
                  { email, password }, { withCredentials: true });    
                
                console.log("Auth store| login: " + resp);

                if (200 <= resp.status || resp.status < 300) {
                  // update pinia state
                  this.user = resp.data;
                  console.log(this.user)

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

