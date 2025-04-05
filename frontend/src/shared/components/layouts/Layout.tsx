import { AppSidebar } from '@components/layouts/AppSidebar';
import { SidebarProvider } from '@ui/sidebar';
import { Toaster } from '@ui/sonner';

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <SidebarProvider>
      <AppSidebar />
      <main className='flex-grow'>{children}</main>
      <Toaster richColors />
    </SidebarProvider>
  );
}
