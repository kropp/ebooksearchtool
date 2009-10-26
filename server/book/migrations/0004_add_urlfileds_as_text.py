
from south.db import db
from django.db import models
from server.book.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Deleting unique_together for [link] on bookfile.
        db.delete_unique('book_bookfile', ['link'])
        
        # Adding field 'BookFile.link_hash'
        db.add_column('book_bookfile', 'link_hash', orm['book.bookfile:link_hash'])
        
        # Changing field 'BookFile.img_link'
        # (to signature: django.db.models.fields.TextField(max_length=4000, null=True))
        db.alter_column('book_bookfile', 'img_link', orm['book.bookfile:img_link'])
        
        # Changing field 'BookFile.link'
        # (to signature: django.db.models.fields.TextField(max_length=4000))
        db.alter_column('book_bookfile', 'link', orm['book.bookfile:link'])
        
        # Changing field 'BookFile.size'
        # (to signature: django.db.models.fields.IntegerField(default=0))
        db.alter_column('book_bookfile', 'size', orm['book.bookfile:size'])
        
    
    
    def backwards(self, orm):
        
        # Deleting field 'BookFile.link_hash'
        db.delete_column('book_bookfile', 'link_hash')
        
        # Changing field 'BookFile.img_link'
        # (to signature: django.db.models.fields.URLField(max_length=255, null=True))
        db.alter_column('book_bookfile', 'img_link', orm['book.bookfile:img_link'])
        
        # Changing field 'BookFile.link'
        # (to signature: django.db.models.fields.URLField(unique=True, max_length=255))
        db.alter_column('book_bookfile', 'link', orm['book.bookfile:link'])
        
        # Changing field 'BookFile.size'
        # (to signature: django.db.models.fields.IntegerField())
        db.alter_column('book_bookfile', 'size', orm['book.bookfile:size'])
        
        # Creating unique_together for [link] on bookfile.
        db.create_unique('book_bookfile', ['link'])
        
    
    
    models = {
        'book.alias': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.annotation': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.TextField', [], {'max_length': '10000'})
        },
        'book.author': {
            'alias': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Alias']"}),
            'book': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Book']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']"})
        },
        'book.book': {
            'annotation': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Annotation']"}),
            'book_file': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.BookFile']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lang': ('django.db.models.fields.CharField', [], {'max_length': '2'}),
            'series': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Series']"}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']"}),
            'title': ('django.db.models.fields.CharField', [], {'max_length': '255'})
        },
        'book.bookfile': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'img_link': ('django.db.models.fields.TextField', [], {'max_length': '4000', 'null': 'True'}),
            'link': ('django.db.models.fields.TextField', [], {'max_length': '4000'}),
            'link_hash': ('django.db.models.fields.CharField', [], {'max_length': '32', 'unique': 'True'}),
            'more_info': ('django.db.models.fields.TextField', [], {'max_length': '10000', 'null': 'True'}),
            'size': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'type': ('django.db.models.fields.CharField', [], {'max_length': '10'})
        },
        'book.series': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.tag': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        }
    }
    
    complete_apps = ['book']
