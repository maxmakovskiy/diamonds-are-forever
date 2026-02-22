import { Profile, ChangePassword } from '@/pages/profile'

export default {
    path: '/profile',
    children: [
        { path: '', component: Profile},
        { path: 'change-password', component: ChangePassword },
    ]
}
