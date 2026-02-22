import { Layout, Items, Details, Lifecycle, AddNewItem } from '@/pages/items';

export default {
    path: '/items',
    component: Layout,
    children: [
        { path: '', component: Items },
        // NOTE:
        // pay attention to the name of param here ("id")
        { path: 'details/:id', component: Details, name: "item_details" },
        { path: 'lifecycle/:id', component: Lifecycle, name: "item_lifecycle" },
        { path: 'add-new', component: AddNewItem },
    ]
};
