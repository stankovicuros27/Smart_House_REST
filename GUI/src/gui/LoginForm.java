/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Uros
 */
public class LoginForm extends javax.swing.JFrame {

    /**
     * Creates new form LoginForm
     */
    public LoginForm() {
        initComponents();
    }
    
    private String userName;
    private String password;
    private int userID;
    
    private void updateUsernameAndPassword() {
        userName = usernameTextField.getText();
        password = passwordTextField.getText();
        userID = Integer.parseInt(userIdTextField.getText());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usernameTextField = new javax.swing.JTextField();
        passwordTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        playSongButton = new javax.swing.JButton();
        getPlaylistButton = new javax.swing.JButton();
        makeAlarmButton = new javax.swing.JButton();
        makeTaskButton = new javax.swing.JButton();
        getTasksButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        userIdTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Smarh House Login");

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        playSongButton.setText("Play song");
        playSongButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playSongButtonActionPerformed(evt);
            }
        });

        getPlaylistButton.setText("Get playlist");
        getPlaylistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getPlaylistButtonActionPerformed(evt);
            }
        });

        makeAlarmButton.setText("Make alarm");
        makeAlarmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeAlarmButtonActionPerformed(evt);
            }
        });

        makeTaskButton.setText("Make task");
        makeTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeTaskButtonActionPerformed(evt);
            }
        });

        getTasksButton.setText("Get tasks");
        getTasksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getTasksButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("UserID");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(playSongButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(getPlaylistButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(makeAlarmButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(makeTaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(getTasksButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(userIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(58, 58, 58)
                                .addComponent(jLabel3)))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playSongButton)
                    .addComponent(getPlaylistButton)
                    .addComponent(makeAlarmButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(makeTaskButton)
                    .addComponent(getTasksButton))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playSongButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playSongButtonActionPerformed
        updateUsernameAndPassword();
        PlaySongForm psf = new PlaySongForm(userName, password, userID);
        psf.setVisible(true);
    }//GEN-LAST:event_playSongButtonActionPerformed

    private void getPlaylistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPlaylistButtonActionPerformed
        updateUsernameAndPassword();
        PlaylistForm pf = new PlaylistForm(userName, password, userID);
        pf.setVisible(true);
    }//GEN-LAST:event_getPlaylistButtonActionPerformed

    private void makeAlarmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeAlarmButtonActionPerformed
        updateUsernameAndPassword();
        CreateAlarmForm caf = new CreateAlarmForm(userName, password, userID);
        caf.setVisible(true);
    }//GEN-LAST:event_makeAlarmButtonActionPerformed

    private void makeTaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeTaskButtonActionPerformed
        updateUsernameAndPassword();
        CreateTaskForm caf = new CreateTaskForm(userName, password, userID);
        caf.setVisible(true);
    }//GEN-LAST:event_makeTaskButtonActionPerformed

    private void getTasksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getTasksButtonActionPerformed
        updateUsernameAndPassword();
        GetTasksForm gtf = new GetTasksForm(userName, password, userID);
        gtf.setVisible(true);
    }//GEN-LAST:event_getTasksButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton getPlaylistButton;
    private javax.swing.JButton getTasksButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton makeAlarmButton;
    private javax.swing.JButton makeTaskButton;
    private javax.swing.JTextField passwordTextField;
    private javax.swing.JButton playSongButton;
    private javax.swing.JTextField userIdTextField;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables
}
