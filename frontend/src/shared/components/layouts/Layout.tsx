import { AppSidebar } from '@/components/layouts/AppSidebar';
import { SidebarProvider } from '@/components/ui/sidebar';
import { Toaster } from '../ui/sonner';

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <SidebarProvider
      style={{
        '--sidebar-width': '240px',
      }}
    >
      <AppSidebar />
      <main className='flex-grow'>{children}</main>
      <Toaster richColors />
    </SidebarProvider>
  );
}
