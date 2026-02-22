import { Layout, Profile, ChangePassword } from '@/pages/profile'

export default {
    path: '/profile',
    component: Layout,
    children: [
        { path: '', component: Profile},
        { path: 'change-password', component: ChangePassword },
    ]
}
