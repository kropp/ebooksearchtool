# -*- coding: utf-8 -*-

from south.db import db
from django.db import models
from django_authopenid.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding model 'Nonce'
        db.create_table('django_authopenid_nonce', (
            ('id', orm['django_authopenid.Nonce:id']),
            ('server_url', orm['django_authopenid.Nonce:server_url']),
            ('timestamp', orm['django_authopenid.Nonce:timestamp']),
            ('salt', orm['django_authopenid.Nonce:salt']),
        ))
        db.send_create_signal('django_authopenid', ['Nonce'])
        
        # Adding model 'UserPasswordQueue'
        db.create_table('django_authopenid_userpasswordqueue', (
            ('id', orm['django_authopenid.UserPasswordQueue:id']),
            ('user', orm['django_authopenid.UserPasswordQueue:user']),
            ('new_password', orm['django_authopenid.UserPasswordQueue:new_password']),
            ('confirm_key', orm['django_authopenid.UserPasswordQueue:confirm_key']),
        ))
        db.send_create_signal('django_authopenid', ['UserPasswordQueue'])
        
        # Adding model 'UserAssociation'
        db.create_table('django_authopenid_userassociation', (
            ('id', orm['django_authopenid.UserAssociation:id']),
            ('openid_url', orm['django_authopenid.UserAssociation:openid_url']),
            ('user', orm['django_authopenid.UserAssociation:user']),
        ))
        db.send_create_signal('django_authopenid', ['UserAssociation'])
        
        # Adding model 'Association'
        db.create_table('django_authopenid_association', (
            ('id', orm['django_authopenid.Association:id']),
            ('server_url', orm['django_authopenid.Association:server_url']),
            ('handle', orm['django_authopenid.Association:handle']),
            ('secret', orm['django_authopenid.Association:secret']),
            ('issued', orm['django_authopenid.Association:issued']),
            ('lifetime', orm['django_authopenid.Association:lifetime']),
            ('assoc_type', orm['django_authopenid.Association:assoc_type']),
        ))
        db.send_create_signal('django_authopenid', ['Association'])
        
    
    
    def backwards(self, orm):
        
        # Deleting model 'Nonce'
        db.delete_table('django_authopenid_nonce')
        
        # Deleting model 'UserPasswordQueue'
        db.delete_table('django_authopenid_userpasswordqueue')
        
        # Deleting model 'UserAssociation'
        db.delete_table('django_authopenid_userassociation')
        
        # Deleting model 'Association'
        db.delete_table('django_authopenid_association')
        
    
    
    models = {
        'auth.group': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '80', 'unique': 'True'}),
            'permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'blank': 'True'})
        },
        'auth.permission': {
            'Meta': {'unique_together': "(('content_type', 'codename'),)"},
            'codename': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'content_type': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['contenttypes.ContentType']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'})
        },
        'auth.user': {
            'date_joined': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'email': ('django.db.models.fields.EmailField', [], {'max_length': '75', 'blank': 'True'}),
            'first_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'groups': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Group']", 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'True', 'blank': 'True'}),
            'is_staff': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'is_superuser': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'last_login': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'last_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'password': ('django.db.models.fields.CharField', [], {'max_length': '128'}),
            'user_permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'blank': 'True'}),
            'username': ('django.db.models.fields.CharField', [], {'max_length': '30', 'unique': 'True'})
        },
        'contenttypes.contenttype': {
            'Meta': {'unique_together': "(('app_label', 'model'),)", 'db_table': "'django_content_type'"},
            'app_label': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'model': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '100'})
        },
        'django_authopenid.association': {
            'assoc_type': ('django.db.models.fields.TextField', [], {'max_length': '64'}),
            'handle': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'issued': ('django.db.models.fields.IntegerField', [], {}),
            'lifetime': ('django.db.models.fields.IntegerField', [], {}),
            'secret': ('django.db.models.fields.TextField', [], {'max_length': '255'}),
            'server_url': ('django.db.models.fields.TextField', [], {'max_length': '2047'})
        },
        'django_authopenid.nonce': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'salt': ('django.db.models.fields.CharField', [], {'max_length': '40'}),
            'server_url': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'timestamp': ('django.db.models.fields.IntegerField', [], {})
        },
        'django_authopenid.userassociation': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'openid_url': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['auth.User']", 'unique': 'True'})
        },
        'django_authopenid.userpasswordqueue': {
            'confirm_key': ('django.db.models.fields.CharField', [], {'max_length': '40'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'new_password': ('django.db.models.fields.CharField', [], {'max_length': '30'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['auth.User']", 'unique': 'True'})
        }
    }
    
    complete_apps = ['django_authopenid']
