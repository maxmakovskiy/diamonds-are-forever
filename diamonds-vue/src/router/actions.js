import { Layout, Actions, ModifyAction, MakeNewAction } from '@/pages/actions';

export default {
    path: '/actions',
    component: Layout,
    children: [
        { path: '', component: Actions },
        { path: 'modify/:id', component: ModifyAction },
        { path: 'make-new', component: MakeNewAction },
    ]
};
