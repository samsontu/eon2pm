/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is EON Guideline Execution Engine.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2009.  All Rights Reserved.
 *
 * The EON Guideline Execution Engine was developed by Center for Biomedical
 * Informatics Research (http://www.bmir.stanford.edu) at the Stanford University
 * with support from the National Library of Medicine.  Current
 * information about the EON project can be obtained at 
 * http://www.smi.stanford.edu/projects/eon/
 *
 */
package edu.stanford.smi.eon.PCAServerModule;
/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Choice_Evaluation
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Choice_Evaluation.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Choice_Evaluation
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Choice_Evaluation:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    union Choice_Evaluation switch(::PCAServerModule::Evaluation_Type) {
      case ::PCAServerModule::add:
        ::PCAServerModule::Add_Evaluation add_eval;
      case ::PCAServerModule::delete:
        ::PCAServerModule::Delete_Evaluation delete_eval;
      case ::PCAServerModule::substitute:
        ::PCAServerModule::Substitute_Evaluation substitute_eval;
      case ::PCAServerModule::change_attribute:
        ::PCAServerModule::Change_Attribute_Evaluation change_attribute_eval;
    };
</pre>
</p>
*/
final public class Choice_Evaluation implements java.io.Serializable {
  private java.lang.Object _object;
  private edu.stanford.smi.eon.PCAServerModule.Evaluation_Type _disc;
  public Choice_Evaluation() {
  }
  public edu.stanford.smi.eon.PCAServerModule.Evaluation_Type discriminator() {
    return _disc;
  }
  public edu.stanford.smi.eon.PCAServerModule.Add_Evaluation add_eval() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.add.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("add_eval");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Add_Evaluation) _object;
  }
  public edu.stanford.smi.eon.PCAServerModule.Delete_Evaluation delete_eval() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.delete.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("delete_eval");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Delete_Evaluation) _object;
  }
  public edu.stanford.smi.eon.PCAServerModule.Substitute_Evaluation substitute_eval() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.substitute.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("substitute_eval");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Substitute_Evaluation) _object;
  }
  public edu.stanford.smi.eon.PCAServerModule.Change_Attribute_Evaluation change_attribute_eval() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.change_attribute.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("change_attribute_eval");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Change_Attribute_Evaluation) _object;
  }
  public void add_eval(edu.stanford.smi.eon.PCAServerModule.Add_Evaluation value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Evaluation_Type) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.add;
    _object = value;
  }
  public void delete_eval(edu.stanford.smi.eon.PCAServerModule.Delete_Evaluation value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Evaluation_Type) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.delete;
    _object = value;
  }
  public void substitute_eval(edu.stanford.smi.eon.PCAServerModule.Substitute_Evaluation value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Evaluation_Type) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.substitute;
    _object = value;
  }
  public void change_attribute_eval(edu.stanford.smi.eon.PCAServerModule.Change_Attribute_Evaluation value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Evaluation_Type) edu.stanford.smi.eon.PCAServerModule.Evaluation_Type.change_attribute;
    _object = value;
  }

}
