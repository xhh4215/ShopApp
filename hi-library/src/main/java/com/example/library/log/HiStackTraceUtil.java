package com.example.library.log;

/***
 * @author 栾桂明
 * @date 2021年12月3日
 * @desc 对堆栈信息的处理
 */
public class HiStackTraceUtil {
    /***
     * 获取真正的裁剪之后的堆栈信息
     * @param stackTrace
     * @param ignorePackName
     * @param maxDepth
     * @return
     */
    public static StackTraceElement[] getCroppedRealStackTrace(StackTraceElement[] stackTrace, String ignorePackName, int maxDepth) {
        return cropStackTrace(getRealStackTrace(stackTrace, ignorePackName), maxDepth);
    }

    /***
     * 获取除忽略包外的堆栈信息
     * @param srcStack
     * @param ignorePackName
     * @return
     */
    private static StackTraceElement[] getRealStackTrace(StackTraceElement[] srcStack, String ignorePackName) {
        int ignoreDepth = 0;
        int allDepth = srcStack.length;
        String className;
        for (int i = allDepth - 1; i >= 0; i--) {
            className = srcStack[i].getClassName();
            if (ignorePackName != null && className.startsWith(ignorePackName)) {
                ignoreDepth = i + 1;
                break;
            }
        }
        int readDepth = allDepth - ignoreDepth;
        StackTraceElement[] realStack = new StackTraceElement[readDepth];
        System.arraycopy(srcStack, ignoreDepth, realStack, 0, readDepth);
        return realStack;
    }

    /***
     * 裁剪堆栈信息
     * @param callStack
     * @param maxDepth
     * @return
     */
    private static StackTraceElement[] cropStackTrace(StackTraceElement[] callStack, int maxDepth) {
        //获取堆栈信息的长度
        int realDepth = callStack.length;
        if (maxDepth > 0) {
            realDepth = Math.min(realDepth, maxDepth);
        }
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(callStack, 0, realStack, 0, realDepth);
        return realStack;
    }


}
