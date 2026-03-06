"use client";

import { CreditCard, Smartphone, Wallet } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { usePayment, type PaymentMethod } from "@/hooks/use-payment";
import { cn } from "@/lib/utils";

const PAYMENT_METHODS: { id: PaymentMethod; label: string; icon: React.ReactNode }[] = [
  { id: "카드", label: "신용/체크카드", icon: <CreditCard className="h-5 w-5" /> },
  { id: "토스페이", label: "토스페이", icon: <Wallet className="h-5 w-5" /> },
  { id: "카카오페이", label: "카카오페이", icon: <Wallet className="h-5 w-5" /> },
  { id: "네이버페이", label: "네이버페이", icon: <Wallet className="h-5 w-5" /> },
  { id: "삼성페이", label: "삼성페이", icon: <Smartphone className="h-5 w-5" /> },
];

interface PaymentModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSuccess?: () => void;
}

export default function PaymentModal({ open, onOpenChange, onSuccess }: PaymentModalProps) {
  const { requestPayment, isLoading, error, clearError, paymentAmount, isClientKeySet } =
    usePayment();

  const handleMethod = async (method: PaymentMethod) => {
    clearError();
    const ok = await requestPayment(method);
    if (ok) {
      onOpenChange(false);
      onSuccess?.();
    }
  };

  return (
    <Dialog open={open} onOpenChange={(o) => { onOpenChange(o); if (!o) clearError(); }}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>종목 분석 이용권 결제</DialogTitle>
          <DialogDescription>
            무료 3회 사용 후 추가 분석을 위해 결제해 주세요. 결제 시 분석 1회가 추가됩니다.
          </DialogDescription>
        </DialogHeader>
        <div className="space-y-4">
          <p className="text-2xl font-bold text-foreground">
            {paymentAmount.toLocaleString("ko-KR")}원
          </p>
          {!isClientKeySet ? (
            <p className="text-sm text-destructive">
              결제 기능이 설정되지 않았습니다. (NEXT_PUBLIC_TOSS_CLIENT_KEY)
            </p>
          ) : (
            <div className="grid grid-cols-2 gap-2">
              {PAYMENT_METHODS.map(({ id, label, icon }) => (
                <Button
                  key={id}
                  type="button"
                  variant="outline"
                  className={cn("h-auto flex-col gap-1 py-3")}
                  disabled={isLoading}
                  onClick={() => handleMethod(id)}
                >
                  {icon}
                  <span className="text-xs font-medium">{label}</span>
                </Button>
              ))}
            </div>
          )}
          {error && (
            <p className="text-sm text-destructive">{error}</p>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}
